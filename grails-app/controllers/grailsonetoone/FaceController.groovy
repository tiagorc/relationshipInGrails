package grailsonetoone

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class FaceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Face.list(params), model:[faceCount: Face.count()]
    }

    def show(Face face) {
        respond face
    }

    def create() {
        respond new Face(params)
    }

    @Transactional
    def save(Face face) {
        if (face == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (face.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond face.errors, view:'create'
            return
        }

        face.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'face.label', default: 'Face'), face.id])
                redirect face
            }
            '*' { respond face, [status: CREATED] }
        }
    }

    def edit(Face face) {
        respond face
    }

    @Transactional
    def update(Face face) {
        if (face == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (face.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond face.errors, view:'edit'
            return
        }

        face.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'face.label', default: 'Face'), face.id])
                redirect face
            }
            '*'{ respond face, [status: OK] }
        }
    }

    @Transactional
    def delete(Face face) {

        if (face == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        face.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'face.label', default: 'Face'), face.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'face.label', default: 'Face'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
