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

        def feeds = [
          [name:'Adam Smith College', url:'http://www.adamsmith.ac.uk/onlineresources/ictassets/xcri/cap.xml'],
          [name:'University of Bolton', url:'http://www.bolton.ac.uk/xcri/xcri.asp'],
          [name:'Bradford College', url:'http://www2.bradfordcollege.ac.uk/bradfordcollege.xml'],
          [name:'Castle College', url:'http://www.castlecollege.ac.uk/castle_xcri.xml'],
          [name:'Derby College', url:'http://www.derby-college.ac.uk/scripts/xcri/derby-college_course_export.xcri.xml'],
          [name:'Edge Hill University', url:'http://www.edgehill.ac.uk/study/courses/xcri'],
          [name:'University of Lincoln', url:'http://www.lincoln.ac.uk/componants/xml/ULprogrammes_xcri.xml (test feed)'],
          [name:'New College Nottingham', url:'http://www.ncn.ac.uk/webservices/xcri/xcrix.asmx/CourseList (test feed)'],
          [name:'North Nottinghamshire College', url:'http://www.nnc.ac.uk/courseleaflets/xcri.aspx'],
          [name:'Northampton College', url:'http://www.northamptoncollege.ac.uk/xcri/'],
          [name:'The Open University', url:'http://www3.open.ac.uk/study/feeds/ou-xcri-cap.xml'],
          [name:'West Nottinghamshire College', url:'http://www.wnc.ac.uk/feeds/courses.xml'],
          [name:'Higher York', url:'http://www.xxp.org/getHYCourses.html']
        ]

        validate(feeds,user,test_aggr);

      }
      else {
        log.debug("Admin user verified");
      }

      log.debug("Bootstrap completed");
    }


    def destroy = {
    }


  def validate(feeds,user,testaggr) {
    feeds.each { feed ->
      def fd = SingleFileDatafeed.findByBaseurl(feed.url) ?: new SingleFileDatafeed(owner:user,
                                                                                    feedname:feed.name,
                                                                                    baseurl:feed.url,
                                                                                    status:1,
                                                                                    target:testaggr).save()
  
    }
  }

}
