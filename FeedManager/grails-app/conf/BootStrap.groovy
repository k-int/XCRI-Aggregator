import org.apache.shiro.crypto.hash.Sha256Hash
import grails.util.GrailsUtil
import ShiroUser


class BootStrap {
    def init = { servletContext ->
      log.debug("Verify default Shiro User");
      def user = ShiroUser.findByUsername("admin")
      if ( user == null ) {
        log.debug("admin user not found.. creating default");
        user = new ShiroUser(username: "admin", passwordHash: new Sha256Hash("password").toHex())
        user.addToPermissions("*:*")
        user.save()
      }
      else {
        log.debug("Admin user verified");
      }
    }
    def destroy = {
    }
}
