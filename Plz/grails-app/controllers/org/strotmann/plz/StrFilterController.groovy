package org.strotmann.plz



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class StrFilterController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        redirect(action: "create", params: params)
    }
	
	def create() {
        respond new StrFilter(params)
    }

	def list(Integer max) {
		def strFilterInstanceList = StrFilter.getMatchesPlz(params)
		
		params.max = Math.min(max ?: 10, 100)
		[strFiltererInstanceList: strFilterInstanceList, strFilterInstanceTotal: StrFilter.count()]
	}
}
