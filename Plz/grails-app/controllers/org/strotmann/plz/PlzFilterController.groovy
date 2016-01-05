package org.strotmann.plz



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PlzFilterController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        redirect(action: "create", params: params)
    }

    def create() {
        respond new PlzFilter(params)
    }

    def list(Integer max) {
		def plzFilterInstanceList = PlzFilter.getMatches(params)
		
		params.max = Math.min(max ?: 10, 100)
		[plzFiltererInstanceList: plzFilterInstanceList, plzFilterInstanceTotal: PlzFilter.count()]
	}
}
