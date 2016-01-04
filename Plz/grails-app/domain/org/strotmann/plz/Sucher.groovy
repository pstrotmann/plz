package org.strotmann.plz

import java.util.List;
import java.util.Map;

class Sucher {
	
	String strasse
	Integer hausnummer
	String zusatz
	Integer postleitzahl
	String ort
	
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
			return getMatchesStrasseHnrOrt(params.strasse, params.hausnummer, params.ort)
		if (l == [0,0,0,0,1]) 
			return getMatchesOrt(params.ort)
		if (l == [0,0,0,1,0])
			return getMatchesPlz(params.postleitzahl)
			
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
			String q = "from Strasse as s where s.plz.id = ${p.id}"
			def strassen = Strasse.findAll(q)
			if (strassen.empty) {
				Sucher s = new Sucher()
				s.postleitzahl = p.plz
				s.ort = p.ort
				sList << s
			}
			else strassen.each {Strasse str -> 
				Sucher s = new Sucher()
				s.postleitzahl = p.plz
				s.ort = p.ort
				s.strasse = str.strasse
				s.hnrVon = str.hnrVon
				s.hnrBis = str.hnrBis
				s.zusVon = str.zusVon
				s.zusBis = str.zusBis
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
			String q = "from Strasse as s where s.plz.id = ${p.id}"
			def strassen = Strasse.findAll(q)
			if (!strassen.empty) {
				
				strassen.each {Strasse str ->
					if (strasse.size() <= str.strasse.size() && strasse.toUpperCase() == str.strasse.substring(0, strasse.size()).toUpperCase()) {
						Sucher s = new Sucher()
						s.postleitzahl = p.plz
						s.ort = p.ort
						s.strasse = str.strasse
						s.hnrVon = str.hnrVon
						s.hnrBis = str.hnrBis
						s.zusVon = str.zusVon
						s.zusBis = str.zusBis
						sList << s
					}
				}
			}
		}
		if (!hnr)
			return sList.sort{a,b -> a.ort <=> b.ort?:(a.strasse<=>b.strasse)?:(a.hnrVon<=>b.hnrVon)}
			
		//Berücksichtigung der Hausnummer incl. gerade/ungerade (Straßenseite)	
		List <Sucher> sHnrList = []
		sList.each {Sucher s ->
			
			def boolean c1 = !s.hnrVon
			def boolean c2, c3
			if (s.hnrVon) {
				c2 = hnr.toInteger() >= s.hnrVon &&  hnr.toInteger() <= s.hnrBis
				c3 = (hnr.toInteger() % 2) == (s.hnrVon % 2)
			}
			
			if (c1 || (c2 && c3))
				sHnrList << s
		}
		sHnrList.sort{a,b -> a.ort <=> b.ort?:(a.strasse<=>b.strasse)?:(a.hnrVon<=>b.hnrVon)}
	}
	
	Boolean getMitStrassen () {
		String q = "from Strasse as s where s.plz.plz = ${postleitzahl}"
		def strassen = Strasse.findAll(q)
		!strassen.empty
	}
	
	String getPlz5(){
		postleitzahl.toString().padLeft(5, "0")
	}
	
}
