package org.strotmann.plz

import grails.transaction.Transactional
import grails.rest.*
import grails.converters.JSON

@Transactional(readOnly = true)
class WebServiceController extends RestfulController {

    static responseFormats = ['json', 'xml']
	
	WebServiceController() {}
	
	//localhost:8080/Plz/WebService/get?ort=Dortmund&strasse=Evinger
	def get(){
		def List<StrPlzOrt> strPlzOrte = []
		Sucher.getMatches(params).each {Sucher s ->
			StrPlzOrt strPlzOrt = new StrPlzOrt()
			strPlzOrt.strasse = s.strasse
			strPlzOrt.hnrVon = s.hnrVon
			strPlzOrt.zusVon = s.zusVon
			strPlzOrt.hnrBis = s.hnrBis
			strPlzOrt.zusBis = s.zusBis
			strPlzOrt.postleitzahl = s.postleitzahl.toString().padLeft(5, "0")
			strPlzOrt.ort = s.ort
			strPlzOrte << strPlzOrt
		}
		render (strPlzOrte as JSON)
	}
	
	//localhost:8080/Plz/WebService/getGrEmpf?ort=Dortmund&grossempfaenger=AOK
	def getGrEmpf(){
		def List<PlzOrtGrEmpf> plzOrtGrEmpfer = []
		SucherGrosskunde.getMatches(params).each {SucherGrosskunde s ->
			PlzOrtGrEmpf plzOrtGrEmpf = new PlzOrtGrEmpf()
			plzOrtGrEmpf.postleitzahl = s.postleitzahl.toString().padLeft(5, "0")
			plzOrtGrEmpf.ort = s.ort
			plzOrtGrEmpf.grossempfaenger = s.grosskunde
			plzOrtGrEmpfer << plzOrtGrEmpf
		}
		render (plzOrtGrEmpfer as JSON)
	}
		
}


