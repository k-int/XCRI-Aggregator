import org.apache.shiro.crypto.hash.Sha256Hash
import grails.util.GrailsUtil
import ShiroUser

import com.k_int.feedmanager.*


class BootStrap {

    def init = { servletContext ->
      log.debug("Verify default Shiro User");
      def user = ShiroUser.findByUsername("admin")
      if ( user == null ) {
        log.debug("admin user not found.. creating default");
        user = new ShiroUser(username: "admin", passwordHash: new Sha256Hash("password").toHex())
        user.addToPermissions("*:*")
        user.save()

        def test_aggr = new AggregationService(baseurl:'http://localhost:8080/repository/upload.json',
                                               identity:'admin',
                                               credentials:'password',
                                               owner:user);

        test_aggr.save()

        def testfeed = new com.k_int.feedmanager.SingleFileDatafeed(owner:user, 
                                                                    feedname:'University Of Lincoln Programmes', 
                                                                    baseurl:'http://www.lincoln.ac.uk/componants/xml/ULprogrammes_xcri.xml',
                                                                    status:1,
                                                                    target:test_aggr)
        testfeed.save()
      }
      else {
        log.debug("Admin user verified");
      }

      log.debug("Bootstrap completed");
    }


    def destroy = {
    }


}
