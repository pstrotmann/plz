package org.strotmann.plz

import java.util.List;
import java.util.Map;

class SucherGrosskunde {
	
	Integer postleitzahl
	String ort
	String grosskunde

    static constraints = {
		postleitzahl(nullable:true)
		ort(nullable:true)
		grosskunde(nullable:true)
    }
	
	static List getMatches(Map params) {
		
		List l = [0,0,0]
		
		def String grEmpf
		
		if (params.postleitzahl) l[0] = 1
		if (params.ort) l[1] = 1
		if (params.grosskunde) {
			 l[2] = 1
			 grEmpf = params.grosskunde
		}
		if (params.grossempfaenger) {
			 l[2] = 1
			 grEmpf = params.grossempfaenger
		}
		if (l == [1,0,0])
			return getMatchesPlz(params.postleitzahl)
		if (l == [0,1,0])
			return getMatchesOrt(params.ort)
		if (l == [0,0,1])
			return getMatchesGrosskunde(grEmpf)
		if (l == [0,1,1])
			return getMatchesOrtGrosskunde(params.ort,grEmpf)
	}
	
	static List getMatchesPlz(String plz) {
		
		List <SucherGrosskunde> sList = []
		Integer hPlz = plz.toInteger()
		def query = Postleitzahl.where {
			plz == hPlz && grosskunde != null
		}
		query.findAll().each {Postleitzahl p ->
			SucherGrosskunde s = new SucherGrosskunde()
			s.postleitzahl = p.plz
			s.ort = p.ort
			s.grosskunde = p.grosskunde
			sList << s
		}
		sList.sort{SucherGrosskunde gk -> gk.grosskunde}
	}
	
	static List getMatchesOrt(String ort) {
		
		List <Sucher> sList = []
		String hOrt = ort+'%'
		def query = Postleitzahl.where {
			grosskunde != null && grosskunde != '...' && ort =~ hOrt
		}
		query.findAll().each {Postleitzahl p ->
			SucherGrosskunde s = new SucherGrosskunde()
			s.postleitzahl = p.plz
			s.ort = p.ort
			s.grosskunde = p.grosskunde
			sList << s
		}
		sList.sort{SucherGrosskunde gk -> gk.grosskunde}
	}
	
	static List getMatchesGrosskunde(String grosskunde) {
		
		List <Sucher> sList = []
		String hGrosskunde = grosskunde+'%'
		def query = Postleitzahl.where {
			grosskunde != null && grosskunde != '...' && grosskunde =~ hGrosskunde
		}
		query.findAll().each {Postleitzahl p ->
			SucherGrosskunde s = new SucherGrosskunde()
			s.postleitzahl = p.plz
			s.ort = p.ort
			s.grosskunde = p.grosskunde
			sList << s
		}
		sList.sort{SucherGrosskunde gk -> gk.ort}
	}
	
	static List getMatchesOrtGrosskunde(String ort, String grosskunde) {
		
		List <Sucher> sList = []
		String hOrt = ort+'%'
		String hGrosskunde = grosskunde+'%'
		def query = Postleitzahl.where {
			grosskunde != null && grosskunde != '...' && ort =~ hOrt && grosskunde =~ hGrosskunde
		}
		query.findAll().each {Postleitzahl p ->
			SucherGrosskunde s = new SucherGrosskunde()
			s.postleitzahl = p.plz
			s.ort = p.ort
			s.grosskunde = p.grosskunde
			sList << s
		}
		sList.sort{SucherGrosskunde gk -> gk.postleitzahl}
	}
	
	String getPlz5(){
		postleitzahl.toString().padLeft(5, "0")
	}
}
