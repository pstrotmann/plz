package org.strotmann.plz

import grails.transaction.Transactional
import grails.rest.*
import grails.converters.JSON

@Transactional(readOnly = true)
class WebServiceController extends RestfulController {

    static responseFormats = ['json', 'xml']
	
	WebServiceController() {}
	
	//localhost:8080/Plz/WebService/get?plz=44339
	def get(){
		def entry = params.find{ k, v -> Postleitzahl.metaClass.hasMetaProperty k }
		if( entry ) {
		  if (entry.key == 'id')
			  entry.value = entry.value.toLong()
		  if (entry.key == 'plz' || entry.key == 'osmId' || entry.key == 'bundesland')
			entry.value = entry.value.toInteger()
			
		  render (Postleitzahl.withCriteria() { eq (entry.key, entry.value) } as JSON)
		}
		 else
		  render text:'not found!'
	}
	
	//localhost:8080/Plz/WebService/getViaOrtStrasse?ort=Dortmund&strasse=Evinger
	def getViaOrtStrasse(){
		def List strPlzOrte = []
		def List plzIds = []
		def Map postleitzahlen = [:]
		def Map orte = [:]
		def entryPlz = params.find{ k, v -> Postleitzahl.metaClass.hasMetaProperty k }
		if( entryPlz ) {
		  if (entryPlz.key == 'ort')
			Postleitzahl.withCriteria() { like (entryPlz.key, "${entryPlz.value}%") }.each {
				if(hatStrVerz (it.id)) { 
					plzIds << it.id
					postleitzahlen[it.id] = it.plz
					orte[it.id] = it.ort
				}
				else {
					StrPlzOrt strPlzOrt = new StrPlzOrt()
					strPlzOrt.postleitzahl = it.plz
					strPlzOrt.ort = it.ort
					strPlzOrte << strPlzOrt
				}
			}
		}
		else
		  render text:'plz not found!'
		def str = params["strasse"]
		String s = "from Strasse as s where s.strasse like '${str}%' and s.plz in ${plzIds}"
		s = s.replace('[', '(')
		s = s.replace(']', ')')
		def strassen = Strasse.findAll (s)
		
		strassen.each {
			StrPlzOrt strPlzOrt = new StrPlzOrt()
			strPlzOrt.strasse = it.strasse
			strPlzOrt.hnrVon = it.hnrVon
			strPlzOrt.zusVon = it.zusVon
			strPlzOrt.hnrBis = it.hnrBis
			strPlzOrt.zusBis = it.zusBis
			strPlzOrt.postleitzahl = postleitzahlen[it.plz.id]
			strPlzOrt.ort = orte[it.plz.id]
			strPlzOrte << strPlzOrt
		}
		render (strPlzOrte as JSON) 
	}
	
	private Boolean hatStrVerz (Long plzId) {
		String s = "from Strasse as s where s.plz.id = ${plzId}"
		def strassen = Strasse.findAll(s)
		!strassen.empty
	}
}


