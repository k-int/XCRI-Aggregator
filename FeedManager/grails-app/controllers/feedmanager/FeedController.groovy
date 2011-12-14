package feedmanager

import com.k_int.feedmanager.*

class FeedController {

  def feedRunnerService

  def index() {
    log.debug("index")
  }

  def collect() {
    log.debug("collect")
    def feedDefinition = Datafeed.get(params.id);
    feedRunnerService.collect(feedDefinition)
  }
}
