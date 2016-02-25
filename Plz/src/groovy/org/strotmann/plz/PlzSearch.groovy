package org.strotmann.plz

import grails.plugins.rest.client.*
import grails.util.Holders

import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray

class PlzSearch {
	
	def RestBuilder rest = new RestBuilder()
	def RestResponse resp
	
	String suchePlz (String hnrStrasse, String ort) {
		resp = rest.get("${Holders.config.nominatimService}/search?street=${hnrStrasse}&city=${ort}&country=Germany&format=json&addressdetails=1")

		String plz 
		JSONArray array = new JSONArray(resp.text)
		for (int i=0;i<array.length();i++) {
			JSONObject jsonObject = array.getJSONObject(i)
			plz = jsonObject["address"]["postcode"]
			println plz
			}
		plz
	}
}
