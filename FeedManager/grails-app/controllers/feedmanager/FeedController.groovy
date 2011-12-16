package feedmanager

import com.k_int.feedmanager.*

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

  
}
