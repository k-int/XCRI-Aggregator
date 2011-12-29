import org.apache.shiro.crypto.hash.Sha256Hash
import grails.util.GrailsUtil
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
          [name:'Adam Smith College', url:'http://www.adamsmith.ac.uk/onlineresources/ictassets/xcri/cap.xml',dp:'AdamSmithCollege'],
          [name:'University of Bolton', url:'http://www.bolton.ac.uk/xcri/xcri.asp',dp:'UoBolton'],
          [name:'Bradford College', url:'http://www2.bradfordcollege.ac.uk/bradfordcollege.xml',dp:'BradfordCollege'],
          [name:'Castle College', url:'http://www.castlecollege.ac.uk/castle_xcri.xml',dp:'CastleCollege'],
          [name:'Derby College', url:'http://www.derby-college.ac.uk/scripts/xcri/derby-college_course_export.xcri.xml',dp:'DerbyCollege'],
          [name:'Edge Hill University', url:'http://www.edgehill.ac.uk/study/courses/xcri',dp:'EdgeHillUni'],
          [name:'University of Lincoln', url:'http://www.lincoln.ac.uk/componants/xml/ULprogrammes_xcri.xml (test feed)',dp:'UoL'],
          [name:'New College Nottingham', url:'http://www.ncn.ac.uk/webservices/xcri/xcrix.asmx/CourseList (test feed)',dp:'NCL'],
          [name:'North Nottinghamshire College', url:'http://www.nnc.ac.uk/courseleaflets/xcri.aspx',dp:'NNC'],
          [name:'Northampton College', url:'http://www.northamptoncollege.ac.uk/xcri/',dp:'NorthamptonCollege'],
          [name:'The Open University', url:'http://www3.open.ac.uk/study/feeds/ou-xcri-cap.xml',dp:'OU'],
          [name:'West Nottinghamshire College', url:'http://www.wnc.ac.uk/feeds/courses.xml',dp:'WNC'],
          [name:'Higher York', url:'http://www.xxp.org/getHYCourses.html',dp:'HY']
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
                                                                                    target:testaggr,
                                                                                    active:true,
                                                                                    lastCheck:0,
                                                                                    checkInterval:864000,
                                                                                    dataProvider:feed.dp).save()
  
    }
  }

}
