package org.strotmann.plz

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SucherController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        redirect(action: "create", params: params)
    }

    def create() {
        [sucherInstance: new Sucher(params)]
    }

	def list(Integer max) {
		def sucherInstanceList = Sucher.getMatches(params)
		
		params.max = Math.min(max ?: 10, 100)
		[sucherInstanceList: sucherInstanceList, sucherInstanceTotal: Sucher.count()]
	}
    
}
