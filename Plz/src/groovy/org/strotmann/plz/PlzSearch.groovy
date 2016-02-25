package org.strotmann.plz

import grails.plugins.rest.client.*
import grails.util.Holders

import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray

class PlzSearch {
	
	def RestBuilder rest = new RestBuilder()
	def RestResponse resp
	
	List <StrPlzOrt> suchePlz (String hnrStrasse, String ort) {
		resp = rest.get("${Holders.config.nominatimService}/search?street=${hnrStrasse}&city=${ort}&country=Germany&format=json&addressdetails=1")
		List plzList = []
		String plz,str,city
		JSONArray array = new JSONArray(resp.text)
		for (int i=0;i<array.length();i++) {
			JSONObject jsonObject = array.getJSONObject(i)
			plz = jsonObject["address"]["postcode"]
			str = jsonObject["address"]["road"]
			city = jsonObject["address"]["city"]
			StrPlzOrt s = new StrPlzOrt(postleitzahl:plz.toInteger(),strasse:str,ort:city)
			if (!inPlzList(plzList,s) && str)
				plzList << s
			}
		plzList.sort{it.postleitzahl}
		
	}
	
	Boolean inPlzList (List l, StrPlzOrt s) {
		boolean r = false
		l.each {StrPlzOrt spo ->
			if (spo.postleitzahl == s.postleitzahl && spo.strasse == s.strasse) 
				r = true
		}
		r
	}
}
