package org.strotmann.plz

import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem

class LoadController {
	
	def index() {
		render ("AdressTabelle wird aus OSM geladen")
		FileInputStream osmStream = new FileInputStream ("/vol/map.osm");
		InputStreamReader osmReader = new InputStreamReader(osmStream, "UTF-8")
		BufferedReader osm = new BufferedReader (osmReader)
		Integer cntRead = 0
		Integer cntLoad = 0
		Integer cntAdr = 0
		List adrL
		Boolean lActive = false
		osm.eachLine {String it ->
			cntRead++
			if (it.trim().startsWith("<node") || it.trim().startsWith("<way")) {
				if (lActive && (adrL != [null,null,null,null]))
					println "Verschachtelung bei it=${it}"
				else {
					adrL = [null,null,null,null]
					lActive = true
				}
			}
			if (it.trim().startsWith("</node") || it.trim().startsWith("</way")) {
				if (adrL[0] && adrL[2] && adrL[3]) {
					def Adresse adresse = new Adresse()
					adresse.ort = adrL[0]
					adresse.hnr = adrL[1]
					adresse.plz = adrL[2]
					adresse.str = adrL[3]
					println adresse
					if (adresse.save(flush:true))
						cntLoad++
					else
						adresse.errors.each {
							println it
						}
				}
					
				lActive = false
			}
			if (it.contains("<tag") && tagK(it) == "addr:city") {
				cntAdr++
				adrL[0] = tagV(it)
			}
			if (it.contains("<tag") && tagK(it) == "addr:housenumber")  
				adrL[1] = hnrPad(tagV(it))
			if (it.contains("<tag") && tagK(it) == "addr:postcode") 
				adrL[2] = tagV(it).toInteger()
			if (it.contains("<tag") && tagK(it) == "addr:street") 
				adrL[3] = tagV(it)
		}
		render (" Adress Tabelle wurde aus OSM geladen, ${cntRead} Zeilen gelesen, ${cntAdr} Adressen gefunden, ${cntLoad} Adressen geladen")
	}
			
	String tagK (String tag) {
		Integer anfKey = tag.trim().indexOf("k=") + 3
		Integer endKey = tag.trim().indexOf('"', anfKey) 
		tag.trim().substring(anfKey, endKey)
	}
	String tagV (String tag) {
		Integer anfKey = tag.trim().indexOf("v=") + 3
		Integer endKey = tag.trim().indexOf('"', anfKey)
		tag.trim().substring(anfKey, endKey)
	}
	
	String hnrPad (String hnr) {
		def String n = ''
		def String a = ''
		if (hnr.contains('-')) {
			n = hnr.split("-")[0]
			a = "-"+hnr.split("-")[1]
		}
		else
		for (Integer i = 0; i < hnr.length(); i++) {
			String s = hnr.substring(i, i+1)
			if (s.isNumber())
				n += s
			else
				a += s
		}
		n = n.padLeft(4,'0') + a
		n
	}
	
	def ladePlzKoeln() {
		render ("Straßen Tabelle wird aus ExcelDatei geladen")
		// öffnen ExcelDatei
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("/vol/strassenKöln.xls"));
		HSSFWorkbook bBkTab = new HSSFWorkbook(fs);
		HSSFSheet bBkSheet = bBkTab.getSheetAt(0);
		for (int i = 1; i < bBkSheet.getLastRowNum(); i++)
		//for (int i = 1; i < 20; i++)
		{
			HSSFRow row = bBkSheet.getRow(i)
			
			params.strasse = row.getCell(0).getStringCellValue()
			
			String q = "from Postleitzahl as p where p.plz = ${row.getCell(1).getNumericCellValue().toInteger()}"
			params.plz = Postleitzahl.find(q)
			
			def List n = []
			def List a = []
			for (int j = 2; j < 6; j++) {
				if (row.getCell(j)) {
					if (row.getCell(j).getCellType() == 0) {
						n[j-2]=row.getCell(j).getNumericCellValue().toInteger()
						a[j-2]=null
					}
					else {
						n[j-2]=row.getCell(j).getStringCellValue().substring(0, 4).toInteger()
						a[j-2]=row.getCell(j).getStringCellValue().substring(4)
					}
				}
				else {
					n[j-2]=null
					a[j-2]=null
				}
			}
			
			if (n[0]) {
				params.hnrVon = n[0]
				params.zusVon = a[0]
				params.hnrBis = n[1]
				params.zusBis = a[1]
				updStr(i)
			}
			if (n[2]) {
				params.hnrVon = n[2]
				params.zusVon = a[2]
				params.hnrBis = n[3]
				params.zusBis = a[3]
				updStr(i)
			}
			if (n == [null,null,null,null]) {
				params.hnrVon = null
				params.zusVon = null
				params.hnrBis = null
				params.zusBis = null
				updStr(i)
			}
		}
		render ("Strassen Tabelle wurde aus ExcelDatei geladen")
	}
	
	private updStr (Integer i) {
		println "i=${i} ${params}"
		try {
			def b = new Strasse (params)
			if (!b.save(flush:true)) {
				b.errors.each {
					println it
					render (it)
				}
			}
							
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
	
	def ladeGrosskunden() {
		render ("Plz Tabelle wird aus ExcelDatei geladen")
		// öffnen ExcelDatei
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("/vol/grosskundenPlz.xls"));
		HSSFWorkbook bBkTab = new HSSFWorkbook(fs);
		HSSFSheet bBkSheet = bBkTab.getSheetAt(0);
		for (int i = 0; i < bBkSheet.getLastRowNum(); i++)
		{
			HSSFRow row = bBkSheet.getRow(i);
			
			params.plz = row.getCell(0).getNumericCellValue().toInteger()
			params.ort = row.getCell(1).getStringCellValue()
			params.gkMerk = row.getCell(2).getStringCellValue()
			params.grosskunde = row.getCell(3).getStringCellValue()
			String hOrt = params.ort+'%'
			def query = Postleitzahl.where {
				ort =~ hOrt
			}
			List <Postleitzahl> plzList = query.findAll()
			if (plzList.empty)
				params.bundesland = 0
			else
				params.bundesland = plzList[0].bundesland
				
			try {
				def b = new Postleitzahl (params)
				if (!b.save(flush:true)) {
					b.errors.each {
						println it
						render (it)
					}
				}
								
			} catch (Exception e) {
				e.printStackTrace()
			}
		}
		render ("PLZ Tabelle wurde aus ExcelDatei geladen")
	}
	
	def ladePlzFfM() {
			render ("Straßen Tabelle wird aus ExcelDatei geladen")
			// öffnen ExcelDatei
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("/vol/strassenFrankfurt2014.xls"));
			HSSFWorkbook bBkTab = new HSSFWorkbook(fs);
			HSSFSheet bBkSheet = bBkTab.getSheetAt(0);
			for (int i = 1; i <= bBkSheet.getLastRowNum(); i++)
			//for (int i = 0; i <= 12; i++)
			{
				HSSFRow row = bBkSheet.getRow(i);
				params.strasse = row.getCell(0).getStringCellValue()
				
				if (!row.getCell(1) || row.getCell(1).getCellType() == 1)
					params.hnrVon = null
				else
					params.hnrVon = row.getCell(1).getNumericCellValue().toInteger();
					
				if (!row.getCell(2) || row.getCell(2).getStringCellValue().trim() == '')
					params.zusVon = null
				else
					params.zusVon = row.getCell(2).getStringCellValue()
					
				if (!row.getCell(3) || row.getCell(3).getCellType() == 1)
					params.hnrBis = null
				else
					params.hnrBis = row.getCell(3).getNumericCellValue().toInteger();
					
				if (!row.getCell(4) || row.getCell(4).getStringCellValue().trim() == '')
					params.zusBis = null
				else
					params.zusBis = row.getCell(4).getStringCellValue()
				
				String q = "from Postleitzahl as p where p.plz = ${row.getCell(5).getNumericCellValue().toInteger()}"
				params.plz = Postleitzahl.find(q)
				try {
					def b = new Strasse (params)
					if (!b.save(flush:true)) {
						b.errors.each {
							println it
							render (it)
						}
					}
									
				} catch (Exception e) {
					e.printStackTrace()
				}
			}
			render ("Strassen Tabelle wurde aus ExcelDatei geladen")
		}
	
	def ladePlz() {
		render ("Plz Tabelle wird aus ExcelDatei geladen")
		// öffnen ExcelDatei
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("/vol/zuordnung_plz_ort.xls"));
		HSSFWorkbook bBkTab = new HSSFWorkbook(fs);
		HSSFSheet bBkSheet = bBkTab.getSheetAt(0);
		for (int i = 1; i <= bBkSheet.getLastRowNum(); i++)
		//for (int i = 1; i <= 10; i++)
		{
			HSSFRow row = bBkSheet.getRow(i);
			if (row.getCell(0).getNumericCellValue().toInteger() > 0)
				params.osmId = row.getCell(0).getNumericCellValue().toInteger()
			else
				params.osmId = null
			params.ort = row.getCell(1).getStringCellValue()
			params.plz = row.getCell(2).getNumericCellValue().toInteger()
			params.bundesland = Postleitzahl.bundeslandId(row.getCell(3).getStringCellValue())
			try {
				def b = new Postleitzahl (params)
				if (!b.save(flush:true)) {
					b.errors.each {
						println it
						render (it)
					}
				}
								
			} catch (Exception e) {
				e.printStackTrace()
			}
		}
		render ("PLZ Tabelle wurde aus ExcelDatei geladen")
	}
	
	def erzeugeStreetWrk () {
		render ("StreetWrk wird aus seq.Datei geladen")
		FileOutputStream fileOutputStream = new FileOutputStream ("grails-app/streetWrk")
		OutputStreamWriter outpuStreamWriter = new OutputStreamWriter (fileOutputStream)
		BufferedWriter streetWrk = new BufferedWriter(outpuStreamWriter)
		
		FileInputStream plzStream = new FileInputStream ("/vol/plzUml.dat");
		InputStreamReader plzReader = new InputStreamReader(plzStream, "ISO-8859-15")
		BufferedReader plz = new BufferedReader (plzReader)
		
		plz.eachLine {String it ->
			
			if (it.substring(0,4) == 'ST99') 
				streetWrk.writeLine(it)
		}
		streetWrk.close()
		render ("StreetWrk wurde aus seq.Datei geladen")
	}
	
	def streetWrk() {
		render ("Strassen Tabelle wird aus StreetWrk geladen")
		FileInputStream streetStream = new FileInputStream ("grails-app/streetWrk");
		InputStreamReader streetReader = new InputStreamReader(streetStream, "UTF-8")
		BufferedReader street = new BufferedReader (streetReader)
		Integer cntRead = 0
		Integer cntLoad = 0
		street.eachLine {String it ->
			
			cntRead++
			Integer plz = it.substring(171, 176).toInteger() 
			String s = "from Postleitzahl as pl where pl.plz = ${plz}"
			Postleitzahl pl = Postleitzahl.find (s)
			if (pl != null) {
				Strasse strasse = new Strasse()
				strasse.plz = pl
				strasse.strasse = it.substring(147,170).trim()
				if (it.substring(170,171) != 'N') {
					strasse.hnrVon = it.substring(36, 40).toInteger()
					if (it.substring(40, 41) == " ")
						strasse.zusVon = null
					else
						strasse.zusVon = it.substring(40, 41).trim()
					if (it.substring(44, 48) == "    ") 
						strasse.hnrBis = 9999
					else
						strasse.hnrBis = it.substring(44, 48).toInteger()
					if (it.substring(48, 49) == " " || it.substring(48, 52) == "Ende")
						strasse.zusBis = null
					else
						strasse.zusBis = it.substring(48, 49).trim()
				}
				cntLoad++
				strasse.save()
			}
			else
				println "plz ${plz} unbekannt"
		}
		render ("Strassen Tabelle wurde aus StreetWrk geladen, ${cntRead} Zeilen gelesen, ${cntLoad} Zeilen geladen")
	}
}
