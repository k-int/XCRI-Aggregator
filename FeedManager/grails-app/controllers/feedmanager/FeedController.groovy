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

  def index() {
    log.debug("index")
  }

  /**
   * Run the feed now, testing, reporting any errors and processing any data
   */
  def collect() {
    log.debug("collect")
    def feedDefinition = feedRunnerService.getDatafeed(params.id)
    feedRunnerService.collect(feedDefinition)
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
	
	// Get hold of some services we might use ;)
	org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()
	

	if (params.title || params.description || params.subject)
	{
	  params.max = Math.min(params.max ? params.int('max') : 10, 100)
	  params.offset = params.offset ? params.int('offset') : 0
	  
	  //dynamically build query_string
	  def search_query_str = new StringBuilder()
	  
	  search_query_str << "provid:" << response.feed?.resourceIdentifier
	  
	  if(params.title){ search_query_str << " AND title:(" << params.title << ")" }
	  if(params.description){ search_query_str << " AND description:(" << params.description << ")" }
	  if(params.subject){ search_query_str << " AND subject:(" << params.subject << ")" }

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
  
}
