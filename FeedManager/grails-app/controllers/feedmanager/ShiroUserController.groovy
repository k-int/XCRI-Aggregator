package feedmanager

import com.k_int.feedmanager.*
import org.apache.shiro.crypto.hash.Sha256Hash

class ShiroUserController 
{
    def index = {
        redirect(action: "list", params: params)
    }

    def list = { 	
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
        def results = ShiroUser.createCriteria().list(max: params.max, offset: params.offset)
        {
            and
            {
                if(params.username) { ilike("username", "%" + params.username + "%") }
                if(params.name) 	{ ilike("name", "%" + params.name + "%") }
                if(params.email) 	{ ilike("email", "%" + params.email + "%") }
                if(params.institution) 	{ ilike("institution", "%" + params.institution + "%") }
                if(params.sort && params.order) { order(params.sort, params.order)}
            }
        }
        [usersList: results,usersTotal:results.totalCount]
    }
	
    def edit = {
        def userInstance = ShiroUser.get(params.id)
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [userInstance: userInstance]
        }
    }
	
    def show = {
        def userInstance = ShiroUser.get(params.id)
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
        else {
            [userInstance: userInstance]
        }
    }
	
    def save = {
        def userInstance = ShiroUser.get(params.id)
        if (userInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (userInstance.version > version) {
					
                    userInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'shiroUser.label', default: 'User')] as Object[], "Another administrator has updated this User while you were editing")
                    render(view: "edit", model: [userInstance: userInstance])
                    return
                }
            }
            if(userInstance.active == false && params.active.equalsIgnoreCase("true"))
            {
                sendMail
                {
                    to userInstance.email
                    from "no-reply@k-int.com" //needs changing and putting into config
                    subject "Account Activated"
                    html g.render(template:"activationMail",  model:[username:params.name])
                }
            }
            
            userInstance.properties = params
            if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'shiroUser.label', default: 'User'), userInstance.id])}"
                redirect(action: "show", id: userInstance.id)
            }
            else {
                render(view: "edit", model: [userInstance: userInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }
	
    def delete = {
        def userInstance = ShiroUser.get(params.id)
        if (userInstance) {
            try {
                userInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'shiroUser.label', default: 'User'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'shiroUser.label', default: 'User'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }
	
    def changePassword = {
        def userInstance = ShiroUser.get(params.id)
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'shiroUser.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [userInstance: userInstance]
        }
    }
	
    def savePassword = {
        if (( params.id != null ) &&
            ( params.password != null ) &&
            ( params.password_confirm != null ) &&
            ( params.password == params.password_confirm ) ) {
            def user = ShiroUser.get(params.id)
            if ( user != null )
            {
                log.debug("${params.new_username} user not found.. creating default");

                user.passwordHash = new Sha256Hash(params.password).toHex()
                user.save(flush: true)
		   
                /* now send email to user so they can verify email and activate their account */
                sendMail
                {
                    to user.email
                    from "no-reply@k-int.com" //needs changing and putting into config
                    subject "Password Changed"
                    html g.render(template:"passwordMail",  model:[newPassword:params.password])
                }
		   
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'shiroUser.label', default: 'User'), user.id])}"
                redirect(action: "show", id: user.id)
            }
            else {
                log.debug("Unable to verify user");
                flash.message = "Unable to verify user"
                render(view: "changePassword")
                return
            }
        }
        else {
            log.warn("No passwords entered or passwords do not match");
            flash.message = "No passwords entered or passwords do not match"
            render(view: "changePassword")
            return
        }
    }
}
