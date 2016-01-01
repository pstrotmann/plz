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
		
		List l = [0,0]
		
		if (params.ort) l[0] = 1
		if (params.grosskunde) l[1] = 1
		
		if (l == [1,0])
			return getMatchesOrt(params.ort)
	}
	
	static List getMatchesOrt(String ort) {
		
		List <Sucher> sList = []
		String hOrt = ort+'%'
		def query = Postleitzahl.where {
			 grosskunde != null && ort =~ hOrt
		}
		query.findAll().each {Postleitzahl p ->
			SucherGrosskunde s = new SucherGrosskunde()
			s.postleitzahl = p.plz
			s.ort = p.ort
			s.grosskunde = p.grosskunde
			sList << s
		}
		sList
	}
}
