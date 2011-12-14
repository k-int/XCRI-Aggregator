package feedmanager

import com.k_int.feedmanager.*

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
    def feedDefinition = Datafeed.get(params.id);
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
