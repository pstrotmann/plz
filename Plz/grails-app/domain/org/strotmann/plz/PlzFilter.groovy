package org.strotmann.plz

import java.util.List;
import java.util.Map;

class PlzFilter {

	String ort
   
	 static constraints = {
		 ort()
     }
	 
	 static List getMatchesOrt(Map params) {
		 String hOrt = params.ort+'%'
		 def query = Postleitzahl.where {
		 	 ort =~ hOrt
		 }
		 query.findAll()
	 }
}
