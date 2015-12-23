package org.strotmann.plz

import java.util.List;
import java.util.Map;

class Sucher {
	
	String strasse
	Integer hausnummer
	String zusatz
	Integer postleitzahl
	String ort

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
		if (params.hausnumer) l[1] = 1
		if (params.zusatz) l[2] = 1
		if (params.postleitzahl) l[3] = 1
		if (params.ort) l[4] = 1
		
		if (l == [0,0,0,0,1]) 
			return getMatchesOrt(params.ort)
		if (l == [0,0,0,1,0])
			return getMatchesPlz(params.postleitzahl)
			
	}
	
	static List getMatchesOrt(String ort) {
		
		String hOrt = ort+'%'
		def query = Postleitzahl.where {
			ort =~ hOrt
		}
		query.findAll()
	} 
	
	static List getMatchesPlz(String plz) {
		
		Integer hPlz = plz.toInteger()
		def query = Postleitzahl.where {
			plz == hPlz
		}
		query.findAll()
	}
}
