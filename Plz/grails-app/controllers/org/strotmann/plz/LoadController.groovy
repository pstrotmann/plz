package org.strotmann.plz

import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem

class LoadController {

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
	
	def index() {
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
