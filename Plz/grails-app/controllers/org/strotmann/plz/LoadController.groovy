package org.strotmann.plz

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.hibernate.SessionFactory;
import java.util.calendar.*

class LoadController {
	SessionFactory sessionFactory
	
	def index() {
		render ("start test HnrSplit")
		List <String> hNrn = Adresse.hNrn('14-19bb')
		println "Adresse.hNrn[0]=${hNrn[0]}"
		println "Adresse.hNrn[1]=${hNrn[1]}"
		println "hNrn.size=${hNrn.size}"
		render ("ende  test HnrSplit")
	}
	def load() {
		render ("AdressTabelle wird zu Strassen verdichtet")
		def List strList = Adresse.strassen
		def List selList = ['Holzwickede','Iserlohn','Kamen','Lünen','Schwerte','Witten','Unna']
		def cntStr = 0, cntLoad = 0
		strList.each {
			cntStr++
			Strasse strasse = new Strasse(postleitzahl:it[1].toInteger(),strasse:it[2],hausNrVon:it[3],hausNrBis:it[4])
			if(it[0] in selList)
				if (strasse.save())
					cntLoad++
					else
						strasse.errors.each {
						println it
					}
				}
		def hibSession = sessionFactory.getCurrentSession()
		assert hibSession != null
		hibSession.flush()
		render ("------AdressTabelle wurde zu ${cntStr} Strassen verdichtet, ${cntLoad} in Strassentabelle geladen ")
	}
	def preload() {
		render ("AdressTabelle wird aus OSM geladen")
		def startZeit = Calendar.instance
		println "timeIs=${startZeit.time}"
		def hibSession = sessionFactory.getCurrentSession()
		assert hibSession != null
		def BigDecimal minlat, maxlat, minlon, maxlon
		//nodeMap aufbauen
		FileInputStream osmStream = new FileInputStream ("/vol/map");
		InputStreamReader osmReader = new InputStreamReader(osmStream, "UTF-8")
		BufferedReader osm = new BufferedReader (osmReader)
		Map nodeMap = [:]
		Integer cntNodeMap = 0
		osm.eachLine {String it ->
			if (it.trim().startsWith("<bounds")) {
				minlat = tagVal(it, "minlat").toBigDecimal()
				maxlat = tagVal(it, "maxlat").toBigDecimal()
				minlon = tagVal(it, "minlon").toBigDecimal()
				maxlon = tagVal(it, "maxlon").toBigDecimal()
				println it 
			}
			if (it.trim().startsWith("<node")) {
				cntNodeMap++
				if (cntNodeMap %1000000 == 00)
					println "cntNodeMap=${cntNodeMap}"
				BigInteger nodeId = tagVal(it,'id').toBigInteger()
				nodeMap[nodeId] = [tagVal(it,'lat').toBigDecimal(),tagVal(it,'lon').toBigDecimal()]
			}
		}
		osm.close()
		println "cntNode=${cntNodeMap}"
		//sichere Adressen aufbauen
		osmStream = new FileInputStream ("/vol/map");
		osmReader = new InputStreamReader(osmStream, "UTF-8")
		osm = new BufferedReader (osmReader)
		Integer cntRead = 0
		Integer cntLoad = 0
		Integer cntAdr = 0
		Integer cntDup = 0
		List adrL
		List <PlzNode> nodeList
		Map plzNodeMap = [:]
		String nodeActive = ""
		BigInteger refActive = 0
		Boolean lActive = false
		osm.eachLine {String it ->
			cntRead++
			if (it.trim().startsWith("<nd"))
				refActive = tagVal(it, "ref").toBigInteger()
			if (it.trim().startsWith("<node") || it.trim().startsWith("<way")) {
				adrL = [null,null,null,null,null]
				lActive = true
				if (it.trim().startsWith("<node"))
					nodeActive = it
			}
			if (it.trim().startsWith("</node") || it.trim().startsWith("</way")) {
				if (!adrL[0] && adrL[2]){
					Postleitzahl plz = Postleitzahl.find ("from Postleitzahl as p where p.plz = ${adrL[2]}")
					if (plz)
						adrL[0] = plz.ort
					else
						println "plz ${adrL[2]} nicht gefunden"
				}
				if (adrL[0] && adrL[1] && adrL[2] && adrL[3]) {
					cntAdr++
					def Adresse adresse = new Adresse(ort:adrL[0],hnr:adrL[1],plz:adrL[2],str:adrL[3])
										
					PlzNode plzNode = new PlzNode(plz:adresse.plz,ort:adresse.ort)
					
					if (it.trim().startsWith("</node")) {
						plzNode.lat = tagVal(nodeActive, "lat").toBigDecimal()
						plzNode.lon = tagVal(nodeActive, "lon").toBigDecimal()
						nodeActive = ""
					}
					else {//</way
						List punkt = nodeMap[refActive]
						plzNode.lat = punkt[0]
						plzNode.lon = punkt[1]
						refActive = 0
					}
					String mapKey = PlzNode.plzNodeKey (minlat,maxlat,minlon,maxlon,plzNode.lat,plzNode.lon)
					if (plzNodeMap[mapKey]) 
						nodeList = plzNodeMap[mapKey]
					else
						nodeList = []
					nodeList << plzNode
					plzNodeMap[mapKey] = nodeList
					
					if (adresse.save())
						cntLoad++
					else
						cntDup++
						
					if (adrL[4]) {
						//2. Adresse bilden
						cntAdr++
						def Adresse adresse2 = new Adresse(ort:adresse.ort,hnr:adrL[4],plz:adresse.plz,str:adresse.str)
						if (adresse2.save())
							cntLoad++
						else
							cntDup++
					}
					
					if (cntRead %1000000 == 0) {
						//hibSession.flush()
						//println "${cntLoad} sichere Adressen geladen, Duplikate nicht geladen: ${cntDup}"
					}
				}
				
				lActive = false
			}
			
			if (it.contains("<tag") && tagVal(it,'k') == "addr:city") 
				adrL[0] = tagVal(it,'v')
			if (it.contains("<tag") && tagVal(it,'k') == "addr:housenumber") {
				adrL[1] = Adresse.hNrn(tagVal(it,'v'))[0]
				adrL[4] = Adresse.hNrn(tagVal(it,'v'))[1]
			}
			if (it.contains("<tag") && tagVal(it,'k') == "addr:postcode") 
				adrL[2] = tagVal(it,'v').toInteger()
			if (it.contains("<tag") && tagVal(it,'k') == "addr:street") 
				adrL[3] = tagVal(it,'v')
		
			if (cntRead %1000000 == 0) {
				println "${cntRead} Sätze gelesen,${cntAdr} sichere Adressen gefunden"
			}
		}
		
		
		//hergeleitete Adressen aufbauen
		println "Start Herleitung"
		osmStream = new FileInputStream ("/vol/map");
		osmReader = new InputStreamReader(osmStream, "UTF-8")
		osm = new BufferedReader (osmReader)
		nodeActive = ""
		refActive = 0
		cntDup = 0
		lActive = false
		Integer cntAdrHerl = 0
		cntRead = 0
		osm.eachLine {String it ->
			cntRead++
			if (it.trim().startsWith("<nd"))
				refActive = tagVal(it, "ref").toBigInteger()
			if (it.trim().startsWith("<node") || it.trim().startsWith("<way")) {
				adrL = [null,null,null,null,null]
				lActive = true
				if (it.trim().startsWith("<node"))
					nodeActive = it
			}
			if (it.trim().startsWith("</node") || it.trim().startsWith("</way")) {
				if (it.trim().startsWith("</node") || it.trim().startsWith("</way")) {
					if (!adrL[0] && adrL[2]){
						Postleitzahl plz = Postleitzahl.find ("from Postleitzahl as p where p.plz = ${adrL[2]}")
						if (plz)
							adrL[0] = plz.ort
						else
							println "plz ${adrL[2]} nicht gefunden"
					}
				}
				if (!adrL[0] && adrL[1] && !adrL[2] && adrL[3]) {
					
					cntAdr++
					cntAdrHerl++
					println "cntAdrHerl=${cntAdrHerl}"
					def Adresse adresse = new Adresse()
					
					List <BigDecimal> punkt
					if (it.trim().startsWith("</node")) {
						punkt = [tagVal(nodeActive, "lat").toBigDecimal(),tagVal(nodeActive, "lon").toBigDecimal()]
						nodeActive = ""
					}
					else {//</way
						punkt = nodeMap[refActive]
						refActive = 0
					}
					//jetzt aus nodeList den nächsten Punkt heraussuchen
					//ort und plz durch Nachbarschaft ermitteln
					BigDecimal bigDec0 = punkt[0]
					BigDecimal bigDec1 = punkt[1]
					String mapKey = PlzNode.plzNodeKey (minlat,maxlat,minlon,maxlon,bigDec0,bigDec1)
					if (plzNodeMap[mapKey]) 
						nodeList = plzNodeMap[mapKey]
					else
					if ((mapKey.toInteger()+1).toString().padLeft(2, '0').substring(0, 2))
						nodeList = plzNodeMap[(mapKey.toInteger()+1).toString().padLeft(2, '0').substring(0, 2)]
					else
						nodeList = plzNodeMap[(mapKey.toInteger()-1).toString().padLeft(2, '0').substring(0, 2)]
					if (nodeList && nodeList.size > 0) {
						PlzNode plzNode = PlzNode.nearestPlzNode(nodeList, punkt)
						adresse.ort = plzNode.ort
						adresse.plz = plzNode.plz
						adresse.hnr = adrL[1]
						adresse.str = adrL[3]
								if (adresse.save()) {
									println "hergeleitet:${adresse}"
								}
								else
									cntDup++
									
									if (adrL[4]) {
										//2. Adresse bilden
										cntAdr++
										def Adresse adresse2 = new Adresse(ort:adresse.ort,hnr:adrL[4],plz:adresse.plz,str:adresse.str)
										if (adresse2.save())
											cntLoad++
										else
											cntDup++
									}
					}
					if (cntAdrHerl %1000 == 00) {
						hibSession.flush()
						println "${cntRead} Sätze gelesen,${cntAdrHerl} hergeleitete Adressen geladen, Duplikate nicht geladen: ${cntDup}"
					}
				}
				
				lActive = false
				
			}
			
			if (it.contains("<tag") && tagVal(it,'k') == "addr:city")
				adrL[0] = tagVal(it,'v')
			if (it.contains("<tag") && tagVal(it,'k') == "addr:housenumber") {
				adrL[1] = Adresse.hNrn(tagVal(it,'v'))[0]
				adrL[4] = Adresse.hNrn(tagVal(it,'v'))[1]
			}
			if (it.contains("<tag") && tagVal(it,'k') == "addr:postcode")
				adrL[2] = tagVal(it,'v').toInteger()
			if (it.contains("<tag") && tagVal(it,'k') == "addr:street")
				adrL[3] = tagVal(it,'v')
		}
		println "hergeleitete Adressen:${cntAdrHerl}"
		hibSession.flush()
		
		def endeZeit = Calendar.instance
		println "endeZeit=${endeZeit.time}"
		println "startZeit=${startZeit.time}"
		render (" Adress Tabelle wurde aus OSM geladen, ${cntRead} Zeilen gelesen, ${cntAdr} Adressen gefunden, ${cntLoad} Adressen geladen")
			
	}

	String tagVal (String line, String x) {
		Integer anfKey = line.trim().indexOf("${x}=") + x.length() + 2
		Integer endKey = line.trim().indexOf('"', anfKey)
		line.trim().substring(anfKey, endKey)
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
