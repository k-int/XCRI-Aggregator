package feedmanager

import org.apache.shiro.SecurityUtils
import com.k_int.feedmanager.*
import grails.converters.*

class HomeController {

  def index() {
	  	
	ShiroUser userInstance = ShiroUser.findByUsername(SecurityUtils.subject?.principal);
	  
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
	
	def results = com.k_int.feedmanager.Datafeed.createCriteria().list(max: params.max, offset: params.offset)
	{
		and
		{
			eq("owner.id", userInstance.id)
			if(params.sort && params.order) { order(params.sort, params.order)}
		}
	}
	
    def response = [:]
    response.feeds = results
    response.feedsTotal = results.totalCount
    response
    
    withFormat {
        html {
            response
          //render(view:pagename,model:result)
        }
        json {
          render response as JSON
        }
      }
  }
}
