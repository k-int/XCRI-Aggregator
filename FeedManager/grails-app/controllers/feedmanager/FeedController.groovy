package feedmanager

import com.k_int.feedmanager.*
import grails.converters.*

/**
 *
 * Whenever this class looks up a feed definition, it should do so through the feedRunnerService.
 * that service will return a clean instance of the feed OR the currently running instance of the 
 * feed if it's currently being executed. This allows easy access to the "Console".
 */
class FeedController {

  def feedRunnerService
  
  def ESWrapperService
  
  def reversemap = ['subject':'subject','provider':'provid']

  def index() {
    log.debug("index")
  }
  
  /**
   * Run the feed now, testing, reporting any errors and processing any data
   */
  def collect() {
    log.debug("collect")
    //if force does not exist then set to false
    params.force = "true";
    // def feedDefinition = feedRunnerService.getDatafeed(params.id)
    feedRunnerService.collect(params.boolean('force'), params.id)
    redirect(controller:"home", action: "index")
  }

  // Allows the user to search any aggregations where the collected
  // resource may be found
  def test() {
  }

  // Show the latest logs from depositing this item
  def latestLogs() {
  }

  // Harvest schedule
  def schedule() {
  }

  def console() {
    def response = [:]
    response.feed = feedRunnerService.getDatafeed(params.id)
    response.id = params.id
    // Convert the JSON object held in the jsonResponse property into some objects we can display

    if ( response.feed != null ) {
      if ( response.feed.jsonResponse != null ) {
        response.lastlog = JSON.parse(response.feed.jsonResponse)
      }
      else {
        log.warn("Lastlog entry of feed is null");
      }
    }
    else {
      log.warn("feed is null");
    }

    response
  }

  def dashboard() {
    def response = [:]
    response.feed = feedRunnerService.getDatafeed(params.id)
    response.id = params.id
    response
  }

  def search() {
    def response = [:]
    response.feed = feedRunnerService.getDatafeed(params.id)
	response.id = params.id
	
    if(!params.subject)  params.subject = []
    else
    {
        params.subject = [params.subject].flatten()
    }
    
	// Get hold of some services we might use ;)
	org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()
	

    if(params != null && params.q != null && params.q.length() > 0)
    {
	  params.max = Math.min(params.max ? params.int('max') : 10, 100)
	  params.offset = params.offset ? params.int('offset') : 0
	  
	  //set prov id
      params.provider = response.feed?.resourceIdentifier
      
      def search_query_str = buildQuery(params)
      
	  def search = esclient.search
	  {
		indices "courses"
		types "course"
		source 
		{
		  from = params.offset
		  size = params.max
		  query
		  {
			  query_string (query: search_query_str)
		  }
          facets {
              subject {
                terms {
                  field = 'subject'
                }
              }
          }
		}
	  }
	  
	  response.searchresult = search.response
	  render(view:'results',model:response)
	}
	else 
	{
		log.debug("No query.. Show search page")
		render(view:'search',model:response)
	}
  }
  
  def buildQuery(params) {

    StringWriter sw = new StringWriter()

   if ( ( params != null ) && ( params.q != null ) )
     sw.write(params.q)
   else
     sw.write("*:*")

    reversemap.each { mapping ->

      log.debug("testing ${mapping.key}");

      if ( params[mapping.key] != null ) {
        if ( params[mapping.key].class == java.util.ArrayList) {
          params[mapping.key].each { p ->  
                sw.write(" AND ")
                sw.write(mapping.value)
                sw.write(":")
                sw.write("\"${p}\"")
          }
        }
        else {
          sw.write(" AND ")
          sw.write(mapping.value)
          sw.write(":")
          sw.write("\"${params[mapping.key]}\"")
        }
      }
    }

    sw.toString()
  }
  
  def edit() {
      def feedInstance = feedRunnerService.getDatafeed(params.id)
      if (!feedInstance) {
          flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'feed.label', default: 'Datafeed'), params.id])}"
          redirect(controller: "home", action: "index")
      }
      else {
          return [feed: feedInstance, id: params.id]
      }
  }
  
  def update() {
      def feedInstance = feedRunnerService.getDatafeed(params.id)
      if (feedInstance) {
          if (params.version) {
              def version = params.version.toLong()
              if (feedInstance.version > version) {
                  
                  feedInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'datafeed.label', default: 'Datafeed')] as Object[], "Another user has updated this feed while you were editing")
                  render(view: "edit", model: [feedInstance: feedInstance])
                  return
              }
          }
          feedInstance.properties = params
          if (!feedInstance.hasErrors() && feedInstance.save(flush: true)) {
              flash.message = "${message(code: 'default.updated.message', args: [message(code: 'datafeed.label', default: 'Datafeed'), feedInstance.id])}"
              redirect(action: "dashboard", id: feedInstance.id)
          }
          else {
              render(view: "edit", model: [feed: feedInstance])
          }
      }
      else {
          flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'datafeed.label', default: 'Datafeed'), params.id])}"
          redirect(controller: "home", action: "index")
      }
  }
  
  def publish() {
    def feedInstance = feedRunnerService.getDatafeed(params.id)
    // null/0==Not published, 1==Pending Publish, 2==Published, 3==Pending withdraw
    int publicationStatus
    log.debug("publish, current status is ${feedInstance.publicationStatus}");

    switch ( feedInstance.publicationStatus ) {
      case 0:
        feedInstance.publicationStatus = 1;
        break;
      case 1:
        feedInstance.publicationStatus = 0;
        break;
      case 2:
        feedInstance.publicationStatus = 3;
        break;
      case 3:
        feedInstance.publicationStatus = 2;
        break;
      default:
        break;
    }

    if (!feedInstance.hasErrors() && feedInstance.save(flush: true)) {
      flash.message = "${message(code: 'default.updated.message', args: [message(code: 'datafeed.label', default: 'Datafeed'), feedInstance.id])}"
      redirect(action: "dashboard", id: feedInstance.id)
    }

    render(view: "dashboard", model: [feed: feedInstance, id:params.id])
  }

}
