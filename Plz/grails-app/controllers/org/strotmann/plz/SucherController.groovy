package org.strotmann.plz

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SucherController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        redirect(action: "create", params: params)
    }
	
	def show() {
		Sucher sucherInstance = flash.sucher
		respond sucherInstance
	}

    def create() {
        [sucherInstance: new Sucher(params)]
    }

	def list(Integer max) {
		def sucherInstanceList = Sucher.getMatches(params)
		if (sucherInstanceList && sucherInstanceList.size() == 1) {
			flash.sucher = sucherInstanceList[0]
			redirect(action: "show", params: params)
		}
		params.max = Math.min(max ?: 10, 100)
		[sucherInstanceList: sucherInstanceList, sucherInstanceTotal: Sucher.count()]
	}
	
	def ortChanged(String ort) {
		session.ort = ort
		def postleitzahlen = []
		
		if ( ort != null && ort.size() > 0 ) {
			String s = ort.substring(0,1).toUpperCase()+ort.substring(1)
			postleitzahlen = Postleitzahl.findAllByOrtLike(s+'%', [order:'ort']).unique{it.ort}
		}
		def List orte = []
		postleitzahlen.each {
			orte << it.ort
		}
		render g.select(name:'ortSelect',	from:orte, noSelection:[null:' '])
	}
    
	def strChanged(String str) {
		String ort = session.ort
		def strassen = []
		String s = null, o = null
		if ( str != null && str.size() > 0 )
			s = str.substring(0,1).toUpperCase()+str.substring(1)
		if ( ort != null && ort.size() > 0 )
			o = ort.substring(0,1).toUpperCase()+ort.substring(1)
		if (s && o)
			strassen = Strasse.findAllByOrtLikeAndStrasseLike(o+'%', s+'%', [order:'strasse']).unique{it.strasse}
		else
			if (o)
				strassen = Strasse.findAllByOrtLike(o+'%', [order:'strasse']).unique{it.strasse}
			else
				strassen = []
		
		def List strNamen = []
		strassen.each {
			strNamen << it.strasse
		}
		render g.select(name:'strasseSelect',	from:strNamen, noSelection:[null:' '])
	}
}
