package feedmanager

import com.k_int.feedmanager.*
import org.apache.shiro.SecurityUtils

class RegisterFeedController {

    def index = { }
		
	def save = {
		
		ShiroUser userInstance = ShiroUser.findByUsername(SecurityUtils.subject?.principal);
		
        //set defaults
		params.owner = userInstance
		params.target = AggregationService.get(1)
        params.active = true //set true, this should mean that it almost immediately gets picked up for harvesting
        params.forceHarvest = false
        params.status = 1
        params.lastCheck = 0
        //check interval set to 1 week
        params.checkInterval = 60*60*24*7*1000  // Sec * Min * Hours * Days * Milliseconds
     
        def feedInstance = new SingleFileDatafeed(params)

        if(SingleFileDatafeed.findByBaseurl(params.baseurl))
        {
            flash.message = "A feed with the URL \'" + params.baseurl + "\' already exists."
            render(view: "index", model: [feed: feedInstance])
        }
        else
        {	
    		if (feedInstance.save(flush: true)) {
    			flash.message = "${message(code: 'default.created.message', args: [message(code: 'datafeed.label', default: 'Feed'), feedInstance.id])}"
    			redirect(controller: "home", action: "index", id: feedInstance.id)
    		}
    		else {
    			flash.message = "Save Failed ${feedInstance.errors}"
    			render(view: "index", model: [feed: feedInstance])
    		}
        }
	}
}
