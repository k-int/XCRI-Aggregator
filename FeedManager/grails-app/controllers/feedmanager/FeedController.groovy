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
    // To restrict a search to a specific provider: "http://localhost:9200/courses/course/_search?q=provid:1304A08UN1"
    // To restrict a search to a specific provider: "http://localhost:9200/courses/course/_search?q=provid:%22lincoln.ac.uk10007151%22"
    response.id = params.id
    response
  }
  
}
