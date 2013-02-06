package feedmanager

import org.apache.shiro.crypto.hash.Sha256Hash
import grails.util.GrailsUtil
import com.k_int.feedmanager.*

class ResetController {

    def index() { }
    
    def changepass
    def processReset(){
        def user = ShiroUser.findByUsername(params.username)
   
        if ( user != null ) {
            flash.message = "An email should arrive in your inbox shortly to allow you to finish the password reset process."
            def checkCache = PasswordReset.findByUsername(user.username)
          
            while(checkCache != null){
                checkCache.delete(flush: true)
                checkCache = PasswordReset.findByUsername(user.username)
            }
            changepass = new PasswordReset(username: params.username, password: new Sha256Hash(params.password).toHex())
            changepass.save()
            sendMail
            {
                to user.email
                from "no-reply@k-int.com" //needs changing and putting into config
                subject "Password reset request"
                html g.render(template:"resetMail",  model:[userInstance:user])
            }
        }
        else {
            log.warn("The user doesn't exist!");
            flash.message = "Sorry, we couldn't find ${params.username} user in the database!"
        }
    }
    def reset(){
        def userInstance = ShiroUser.get(params.id)
        def reset = PasswordReset.findByUsername(userInstance.username)
        if (reset != null){
            ShiroUser.executeUpdate("UPDATE ShiroUser SET password_hash='${reset.password}' WHERE username='${reset.username}'");
            reset.delete(flush: true)
            flash.message = "Your password has been reset successfully!"
        }
        else {
            log.warn("Tried to reset without requesting a new password!");
            flash.message = "Sorry, we couldn't reset your password. Are you sure you requested this change?"
        }
    }
}
