package feedmanager

class FrontpageController {

  def index() {
    log.debug("Frontpage Index");
    def response = [:]
    response.feeds = com.k_int.feedmanager.Datafeed.findAll()
    response
  }
}
