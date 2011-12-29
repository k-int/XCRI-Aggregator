package feedmanager

import org.apache.shiro.crypto.hash.Sha256Hash
import grails.util.GrailsUtil
import com.k_int.feedmanager.*

class RegisterController {

  def index() { 
    log.debug("index");
  }

  def processRegistration() {
    log.debug("processRegistration for ${params.new_username}");
    if ( ( params.new_username != null ) &&
         ( params.new_password != null ) &&
         ( params.new_password == params.new_confirmation ) ) {
      def user = ShiroUser.findByUsername(params.new_username)
      if ( user == null ) {
        log.debug("${params.new_username} user not found.. creating default");
        user = new ShiroUser(username: params.new_username, passwordHash: new Sha256Hash(params.new_password).toHex())
        user.addToPermissions("*:*")
        user.save()
      }
      else {
        log.debug("Admin user verified");
      }
    }
    else {
      log.warn("Empty username, password or password != verification");
    }
  }

}
