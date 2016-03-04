package org.strotmann.plz

import grails.plugins.rest.client.*
import grails.util.Holders

import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray

class PlzSearch {
	
	def RestBuilder rest = new RestBuilder()
	def RestResponse resp
	
	List <Sucher> suchePlz (String hnrStrasse, String ort) {
		resp = rest.get("${Holders.config.nominatimService}/search?street=${hnrStrasse}&city=${ort}&country=Deutschland&format=json&addressdetails=1")
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
			
			if (adrObj["city"] && ortOk(adrObj["city"].toString(),ort))
				city = adrObj["city"].toString() 
			else if (adrObj["town"] && ortOk(adrObj["town"].toString(),ort))
					city = adrObj["town"].toString()
				else if (adrObj["county"] && ortOk(adrObj["county"].toString(),ort))
						city = adrObj["county"].toString()
					else if (adrObj["village"] && ortOk(adrObj["village"].toString(),ort))
							city = adrObj["village"].toString()
			
			ortsteil = adrObj["suburb"]
			
			if ( plz && str && city) {
				Sucher s = new Sucher(postleitzahl:plz.toInteger(),strasse:str,ort:city,ortsteil:ortsteil,adrObj:adrObj.toString())
				if(!inPlzList(plzList,s) && strOk(hnrStrasse, str))
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
	
	Boolean strOk (String hnrStr, String str) {
		String vgl1, vgl2
		List <String> s1 = hnrStr.split()
		if (s1[0].isNumber())
			vgl1 = s1[1]
		else
			vgl1 = s1[0]
		List <String> s2 = str.split("-| ")
		vgl2 = s2[0]
		Integer l = vgl1.size() < vgl2.size() ? vgl1.size() : vgl2.size()
		if (vgl1.substring(0, l-1).trim().toUpperCase() == vgl2.substring(0, l-1).trim().toUpperCase())
			return true
		else
			return false
	}
	
	Boolean ortOk (String city, String ort) {
		String vgl1, vgl2
		List <String> s1 = ort.split("-| ")
		vgl1 = s1[0]
		List <String> s2 = city.split("-| ")
		vgl2 = s2[0]
		Integer l = vgl1.size() < vgl2.size() ? vgl1.size() : vgl2.size()
		if (vgl1.substring(0, l-1).trim().toUpperCase() == vgl2.substring(0, l-1).trim().toUpperCase())
			return true
		else
			return false
	}
}
