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
	
	def ortChanged(String ort) {
		def subCategories = []
		
		if ( ort != null ) {
			String s = ort.substring(0,1).toUpperCase()+ort.substring(1)
			subCategories = Postleitzahl.findAllByOrtLike(s+'%', [order:'ort']).unique{it.ort}
		}
//		render g.select(id:'subCategory', name:'subCategory.id',
//			from:subCategories, optionKey:'id', noSelection:[null:' ']
//		)
		def List orte = []
		subCategories.each {
			orte << it.ort
		}
		render g.select(name:'ort',	from:orte, noSelection:[null:' '])
	}
    
}
