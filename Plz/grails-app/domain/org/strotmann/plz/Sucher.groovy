package org.strotmann.plz

import java.util.List;
import java.util.Map;

class Sucher {
	
	String strasse
	Integer hausnummer
	String zusatz
	Integer postleitzahl
	String ort
	Ortsteil ortsteil
	Integer strasseId
	
	Integer hnrVon
	String zusVon
	Integer hnrBis
	String zusBis

    static constraints = {
		strasse(nullable:true)
		hausnummer(nullable:true)
		zusatz(nullable:true)
		postleitzahl(nullable:true)
		ort(nullable:true)
    }
	
	static List getMatches(Map params) {
		
		List l = [0,0,0,0,0]
		
		if (params.strasse) l[0] = 1
		if (params.hausnummer) l[1] = 1
		if (params.zusatz) l[2] = 1
		if (params.postleitzahl) l[3] = 1
		if (params.ort) l[4] = 1
		if (l == [1,0,0,0,1] || l == [1,1,0,0,1]) 
			//return getMatchesStrasseHnrOrt(params.strasse, params.hausnummer, params.ort)
			return plzZuStrasseHnrOrt(params.strasse, params.hausnummer, params.ort)
		if (l == [0,0,0,0,1]) 
			return getMatchesOrt(params.ort)
		if (l == [0,0,0,1,0])
			return getMatchesPlz(params.postleitzahl)
		if (l == [0,0,0,1,1])
			return getMatchesPlzOrt(params.postleitzahl,params.ort)
			
	}
	
	static List getMatchesOrt(String ort) {
		
		List <Sucher> sList = []
		String hOrt = ort+'%'
		def query = Postleitzahl.where {
			ort =~ hOrt && grosskunde == null
		}
		query.findAll().each {Postleitzahl p ->
			Sucher s = new Sucher()
			s.postleitzahl = p.plz
			s.ort = p.ort
			sList << s
		}
		sList
	} 
	
	static List getMatchesPlz(String plz) {
		
		List <Sucher> sList = []
		Integer hPlz = plz.toInteger()
		def query = Postleitzahl.where {
			plz == hPlz && grosskunde == null
		}
		query.findAll().each {Postleitzahl p ->
			String q = "from Strasse as s where s.postleitzahl = ${p.plz}"
			def strassen = Strasse.findAll(q)
			if (strassen.empty) {
				Sucher s = new Sucher()
				s.postleitzahl = p.plz
				s.ort = p.ort
				sList << s
			}
			else strassen.each {Strasse str -> 
				if (str.ort== p.ort) {
					Sucher s = new Sucher(postleitzahl:p.plz,ort:p.ort,strasse:str.strasse,
								hnrVon:hnrN(str.hausNrVon),hnrBis:hnrN(str.hausNrBis),
								zusVon:hnrA(str.hausNrVon),zusBis:hnrA(str.hausNrBis),
								ortsteil:str.ortsteil,strasseId:str.id)
					sList << s
				}
			}
		}
		sList.sort{it.strasse}
	}
	
	static List getMatchesPlzOrt(String plz, String ort) {
		
		List <Sucher> sList = []
		Integer hPlz = plz.toInteger()
		String hOrt = ort
		def query = Postleitzahl.where {
			plz == hPlz && ort == hOrt && grosskunde == null
		}
		Postleitzahl p = query.find()
		String q = "from Strasse as s where s.postleitzahl = ${p.plz}"
		def strassen = Strasse.findAll(q)
		if (strassen.empty) {
			Sucher s = new Sucher()
			s.postleitzahl = p.plz
			s.ort = p.ort
			sList << s
		}
		else strassen.each {Strasse str ->
			if (str.ort== p.ort) {
				Sucher s = new Sucher(postleitzahl:p.plz,ort:p.ort,strasse:str.strasse,
							hnrVon:hnrN(str.hausNrVon),hnrBis:hnrN(str.hausNrBis),
							zusVon:hnrA(str.hausNrVon),zusBis:hnrA(str.hausNrBis),
							ortsteil:str.ortsteil,strasseId:str.id)
				sList << s
			}
		}
		
		sList.sort{it.strasse}
	}
		
	static List getMatchesStrasseHnrOrt(String strasse, String hnr, String ort) {
		
		List <Sucher> sList = []
		String hOrt = ort+'%'
		def query = Postleitzahl.where {
			ort =~ hOrt && grosskunde == null
		}
		query.findAll().each {Postleitzahl p ->
			String q = "from Strasse as s where s.postleitzahl = ${p.plz}"
			def strassen = Strasse.findAll(q)
			if (!strassen.empty) {
				
				strassen.each {Strasse str ->
					if (strasse.size() <= str.strasse.size() && strasse.toUpperCase() == str.strasse.substring(0, strasse.size()).toUpperCase()) {
						Sucher s = new Sucher(postleitzahl:p.plz,ort:p.ort,strasse:str.strasse,
									hnrVon:hnrN(str.hausNrVon),hnrBis:hnrN(str.hausNrBis),
									zusVon:hnrA(str.hausNrVon),zusBis:hnrA(str.hausNrBis),
									ortsteil:str.ortsteil,strasseId:str.id)
						sList << s
					}
				}
			}
		}
		if (!hnr)
			return sList.sort{a,b -> a.ort <=> b.ort?:(a.strasse<=>b.strasse)?:(a.hnrVon<=>b.hnrVon)}
			
		//Berücksichtigung der Hausnummer 
		List <Sucher> sHnrList = []
		sList.each {Sucher s ->
			
			def boolean c1 = !s.hnrVon
			def boolean c2
			if (s.hnrVon) 
				c2 = hnr.toInteger() >= s.hnrVon &&  hnr.toInteger() <= s.hnrBis
			if (c1 || c2)
				sHnrList << s
		}
		sHnrList.sort{a,b -> a.ort <=> b.ort?:(a.strasse<=>b.strasse)?:(a.hnrVon<=>b.hnrVon)}
	}
	
	static List plzZuStrasseHnrOrt(String strasse, String hnr, String ort) {
		List <Sucher> sList = []
		PlzSearch plzSearch = new PlzSearch()
		plzSearch.suchePlz("${hnr} ${strasse}", ort).each {
			Sucher s = new Sucher(postleitzahl:it.postleitzahl,strasse:it.strasse,ort:it.ort)
			sList << s
		}
		sList
	}
	
	Boolean getMitStrassen () {
		String q = "from Strasse as s where s.postleitzahl = ${postleitzahl}"
		def strassen = Strasse.findAll(q)
		!strassen.empty
	}
	
	String getPlz5(){
		postleitzahl.toString().padLeft(5, "0")
	}
	
	String getOrtsteilName() {
		if (!ortsteil)
			return null
		else
			if (ortsteil.name == ort)
				return null
			else
				return ortsteil.name
	}
	
	static Integer hnrN (String hnr) {
		if (!hnr) return null
		def String n = ''
		for (Integer i = 0; i < hnr.length(); i++) {
			String s = hnr.trim().substring(i, i+1)
			if (s.isNumber())
				n += s
		}
		n.toInteger()
	}
	
	static String hnrA (String hnr) {
		if (!hnr) return null
		def String a = ''
		for (Integer i = 0; i < hnr.length(); i++) {
			String s = hnr.trim().substring(i, i+1)
			if (!s.isNumber())
				a += s
		}
		a
	}
	
}
