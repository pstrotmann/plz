package org.strotmann.plz

import grails.plugins.rest.client.*
import grails.util.Holders

import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray

class PlzSearch {
	
	def RestBuilder rest = new RestBuilder()
	def RestResponse resp
	
	List <Sucher> suchePlz (String hnrStrasse, String ort) {
		resp = rest.get("${Holders.config.nominatimService}/search?street=${hnrStrasse}&city=${ort}&country=Germany&format=json&addressdetails=1")
		List <Sucher> plzList = []
		
		JSONArray array = new JSONArray(resp.text)
		for (int i=0;i<array.length();i++) {
			
			String plz=null,str=null,city=null,ortsteil=null
			
			JSONObject jsonObject = array.getJSONObject(i)
			JSONObject adrObj = jsonObject["address"]
			
			plz = (adrObj["postcode"]).toString()
			if (!plz.isNumber())
				plz = null
				
			str = adrObj["road"]
			
			if (adrObj["city"] && adrObj["city"].toString().toUpperCase() == ort.toUpperCase())
				city = adrObj["city"].toString() 
			else if (adrObj["town"] && adrObj["town"].toString().toUpperCase() == ort.toUpperCase())
					city = adrObj["town"].toString()
					else if (adrObj["county"] && adrObj["county"].toString().toUpperCase() == ort.toUpperCase())
							city = adrObj["county"].toString()
			println "city=${city}"
			
			ortsteil = adrObj["suburb"]
			
			if ( plz && str && city) {
				Sucher s = new Sucher(postleitzahl:plz.toInteger(),strasse:str,ort:city,ortsteil:ortsteil)
				if(!inPlzList(plzList,s))
					plzList << s
			}
		}
		plzList.sort{it.postleitzahl}
		
	}
	
	Boolean inPlzList (List l, Sucher s) {
		boolean r = false
		l.each {Sucher su ->
			if (su.postleitzahl == s.postleitzahl && su.strasse == s.strasse) 
				r = true
		}
		r
	}
}
