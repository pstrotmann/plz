package org.strotmann.plz

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PostleitzahlController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
   def index(Integer max) {
	   //params.max = Math.min(max ?: 10, 100)
       respond PlzFilter.getMatchesOrt(params)
    }

    def show(Postleitzahl postleitzahlInstance) {
        respond postleitzahlInstance
    }

    def create() {
        respond new Postleitzahl(params)
    }

    @Transactional
    def save(Postleitzahl postleitzahlInstance) {
        if (postleitzahlInstance == null) {
            notFound()
            return
        }

        if (postleitzahlInstance.hasErrors()) {
            respond postleitzahlInstance.errors, view:'create'
            return
        }

        postleitzahlInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'postleitzahl.label', default: 'Postleitzahl'), postleitzahlInstance.id])
                redirect postleitzahlInstance
            }
            '*' { respond postleitzahlInstance, [status: CREATED] }
        }
    }

    def edit(Postleitzahl postleitzahlInstance) {
        respond postleitzahlInstance
    }

    @Transactional
    def update(Postleitzahl postleitzahlInstance) {
        if (postleitzahlInstance == null) {
            notFound()
            return
        }

        if (postleitzahlInstance.hasErrors()) {
            respond postleitzahlInstance.errors, view:'edit'
            return
        }

        postleitzahlInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Postleitzahl.label', default: 'Postleitzahl'), postleitzahlInstance.id])
                redirect postleitzahlInstance
            }
            '*'{ respond postleitzahlInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Postleitzahl postleitzahlInstance) {

        if (postleitzahlInstance == null) {
            notFound()
            return
        }

        postleitzahlInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Postleitzahl.label', default: 'Postleitzahl'), postleitzahlInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'postleitzahl.label', default: 'Postleitzahl'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
