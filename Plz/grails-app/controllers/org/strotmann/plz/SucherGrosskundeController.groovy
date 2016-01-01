package org.strotmann.plz



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SucherGrosskundeController {

    static allowedMethods = [save: "POST"]

    def index(Integer max) {
        redirect(action: "create", params: params)
    }

    def create() {
        [sucherGrosskundeInstance: new SucherGrosskunde(params)]
    }

	def list(Integer max) {
		def sucherGrosskundeInstanceList = SucherGrosskunde.getMatches(params)
		
		params.max = Math.min(max ?: 10, 100)
		[sucherGrosskundeInstanceList: sucherGrosskundeInstanceList, sucherGrosskundeInstanceTotal: Sucher.count()]
	}
    
}
