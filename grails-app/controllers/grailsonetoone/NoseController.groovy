package grailsonetoone

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class NoseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Nose.list(params), model:[noseCount: Nose.count()]
    }

    def show(Nose nose) {
        respond nose
    }

    def create() {
        respond new Nose(params)
    }

    @Transactional
    def save(Nose nose) {
        if (nose == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (nose.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond nose.errors, view:'create'
            return
        }

        nose.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'nose.label', default: 'Nose'), nose.id])
                redirect nose
            }
            '*' { respond nose, [status: CREATED] }
        }
    }

    def edit(Nose nose) {
        respond nose
    }

    @Transactional
    def update(Nose nose) {
        if (nose == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (nose.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond nose.errors, view:'edit'
            return
        }

        nose.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'nose.label', default: 'Nose'), nose.id])
                redirect nose
            }
            '*'{ respond nose, [status: OK] }
        }
    }

    @Transactional
    def delete(Nose nose) {

        if (nose == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        nose.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'nose.label', default: 'Nose'), nose.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'nose.label', default: 'Nose'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
