package feedmanager

class FrontpageController {

  def index() {
    def response = [:]
    response.user_feeds = com.k_int.feedmanager.Datafeed.findAll()
    response
  }
}