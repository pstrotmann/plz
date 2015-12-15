package org.strotmann.plz



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class StrasseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Strasse.list(params), model:[strasseInstanceCount: Strasse.count()]
    }

    def show(Strasse strasseInstance) {
        respond strasseInstance
    }

    def create() {
        respond new Strasse(params)
    }

    @Transactional
    def save(Strasse strasseInstance) {
        if (strasseInstance == null) {
            notFound()
            return
        }

        if (strasseInstance.hasErrors()) {
            respond strasseInstance.errors, view:'create'
            return
        }

        strasseInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'strasse.label', default: 'Strasse'), strasseInstance.id])
                redirect strasseInstance
            }
            '*' { respond strasseInstance, [status: CREATED] }
        }
    }

    def edit(Strasse strasseInstance) {
        respond strasseInstance
    }

    @Transactional
    def update(Strasse strasseInstance) {
        if (strasseInstance == null) {
            notFound()
            return
        }

        if (strasseInstance.hasErrors()) {
            respond strasseInstance.errors, view:'edit'
            return
        }

        strasseInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Strasse.label', default: 'Strasse'), strasseInstance.id])
                redirect strasseInstance
            }
            '*'{ respond strasseInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Strasse strasseInstance) {

        if (strasseInstance == null) {
            notFound()
            return
        }

        strasseInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Strasse.label', default: 'Strasse'), strasseInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'strasse.label', default: 'Strasse'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
