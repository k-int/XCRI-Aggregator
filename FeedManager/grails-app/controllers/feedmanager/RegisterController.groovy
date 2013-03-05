package feedmanager

import org.apache.shiro.crypto.hash.Sha256Hash
import grails.util.GrailsUtil
import com.k_int.feedmanager.*

class RegisterController {

    def index() { 
        log.debug("index");
    }

    def processRegistration() {
        log.debug("processRegistration for ${params.username}");
        if ( ( params.username != null ) &&
            ( params.password != null ) &&
            ( params.new_email != null ) &&
            ( params.password == params.new_confirmation ) ) {
            def user = ShiroUser.findByUsername(params.username)
            if ( user == null ) {
                log.debug("${params.username} user not found.. creating default");
                user = new ShiroUser(username: params.username, name: params.new_name, email: params.new_email, institution: params.new_institution, passwordHash: new Sha256Hash(params.password).toHex())
                user.addToPermissions("*:*")
                user.save()
		
		/* now send email to user so they can verify email and activate their account */
		sendMail
		{
                    to user.email
                    from "no-reply@k-int.com" //needs changing and putting into config
                    subject "User Account Activation Required"
                    html g.render(template:"activationMail",  model:[userInstance:user])
                }
            }
            else {
                log.debug("Admin user verified");
            }
        }
        else {
            log.warn("Empty username, password or password != verification");
        }
    }
  
    def verify()
    {
        def userInstance = ShiroUser.get(params.id)
	  
        if (userInstance != null)
        {
            userInstance.verified = Boolean.TRUE
            userInstance.save()
            flash.message = "Hello ${userInstance.name}, your account has been successfully activated. Once it has been approved by an administrator you will be able to log in."
        }
        else
        {
            log.warn("No matching user found so unable to verify");
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'ShiroUser'), params.id])}"
        }
    }

}
