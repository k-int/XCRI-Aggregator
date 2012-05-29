import org.apache.shiro.crypto.hash.Sha256Hash
import grails.util.GrailsUtil
import com.k_int.feedmanager.*
import org.codehaus.groovy.grails.commons.ApplicationHolder

class BootStrap {

    def init = { servletContext ->
      log.debug("Verify default Shiro User");
      
      def adminRole = ShiroRole.findByName("Administrator")
      
      if(adminRole == null){
          adminRole = new ShiroRole(name: "Administrator")
          adminRole.addToPermissions("*:*")
          adminRole.save(flush:true, failOnError:true)
          log.debug adminRole.dump()
      }
  
      def user = ShiroUser.findByUsername("admin")

      if ( ApplicationHolder.application.config.feedmanager.default.aggr ) {
        def aggr = AggregationService.findByBaseurl(ApplicationHolder.application.config.feedmanager.default.aggr)
        if ( !aggr ) {
          def aggr = new AggregationService(baseurl:ApplicationHolder.application.config.feedmanager.default.aggr,
                                                 identity:'admin',
                                                 credentials:'password',
                                                 owner:user).save();
        }
      }
     
      if ( user == null ) {
		  
	log.error("${ApplicationHolder.application.config.locations}")
        log.debug("admin user not found.. creating default");
        user = new ShiroUser(username: "admin", name: "Mr Administrator", passwordHash: new Sha256Hash("password").toHex(), email: "email@somedomain.com", verified: Boolean.TRUE, active: Boolean.TRUE)
        user.addToPermissions("*:*")
        user.addToRoles(adminRole)
        user.save()

          // XXP Feeds via /services/service.asmx/getXCRI?strUKPRN=strin.. 
          def feeds = [
            [name:'Adam Smith College', url:'http://www.adamsmith.ac.uk/onlineresources/ictassets/xcri/cap.xml',dp:'AdamSmithCollege'],
            [name:'University of Bolton', url:'http://www.bolton.ac.uk/xcri/xcri.asp',dp:'UoBolton'],
            [name:'Bradford College', url:'http://www2.bradfordcollege.ac.uk/bradfordcollege.xml',dp:'BradfordCollege'],
            [name:'Castle College', url:'http://www.castlecollege.ac.uk/castle_xcri.xml',dp:'CastleCollege'],
            [name:'Derby College', url:'http://www.derby-college.ac.uk/scripts/xcri/derby-college_course_export.xcri.xml',dp:'DerbyCollege'],
            [name:'Edge Hill University', url:'http://www.edgehill.ac.uk/study/courses/xcri',dp:'EdgeHillUni'],
            [name:'University of Lincoln', url:'http://www.lincoln.ac.uk/componants/xml/ULprogrammes_xcri.xml',dp:'UoL'],
            [name:'New College Nottingham', url:'http://www.ncn.ac.uk/webservices/xcri/xcrix.asmx/CourseList',dp:'NCL'],
            [name:'North Nottinghamshire College', url:'http://www.nnc.ac.uk/courseleaflets/xcri.aspx',dp:'NNC'],
            [name:'Northampton College', url:'http://www.northamptoncollege.ac.uk/xcri/',dp:'NorthamptonCollege'],
            [name:'The Open University', url:'http://www3.open.ac.uk/study/feeds/ou-xcri-cap.xml',dp:'OU'],
            [name:'West Nottinghamshire College', url:'http://www.wnc.ac.uk/feeds/courses.xml',dp:'WNC'],
            [name:'Higher York', url:'http://www.xxp.org/getHYCourses.html',dp:'HY']
          ]

      }
      else {
        log.debug("Admin user verified");
        
        user.addToRoles(adminRole)
        user.save()
      }

      log.debug("Bootstrap completed");
    }

    def destroy = {
    }


  def validate(feeds,user,testaggr) {
    feeds.each { feed ->
      def fd = SingleFileDatafeed.findByBaseurl(feed.url)
	  
	  if(!fd)
	  {
		  SingleFileDatafeed sdf = new SingleFileDatafeed(	owner:user,
	                                                        feedname:feed.name,
	                                                        baseurl:feed.url,
	                                                        status:1,
	                                                        target:testaggr,
	                                                        active:true,
	                                                        lastCheck:0,
	                                                        checkInterval:60*60*24*7*100,  // Sec * Min * Hours * Days * Milliseconds
	                                                        dataProvider:feed.dp)
		 		 
		  if (sdf.save(flush: true)) 
		  {
			  log.debug("Feed Save Success");
		  }
		  else
		  {
			  log.error("Save Failed ${sdf.errors}");
		  }
  
	  }
	  else
	  {
		  log.debug("Feed exists");
	  }
	}
  }
}
