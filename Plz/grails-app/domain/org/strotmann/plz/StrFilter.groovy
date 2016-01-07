package org.strotmann.plz

import java.util.List;
import java.util.Map;

class StrFilter {

	Integer plz
	
	static constraints = {
		plz()
    }
	
	static List getMatchesPlz(Map params) {
		Integer hPlz = params.plz.toInteger()
		def query = Strasse.where {
			postleitzahl == hPlz 
		}
		query.findAll()
	}
}
