package feedmanager

class HomeController {

  def index() {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
	
	def results = com.k_int.feedmanager.Datafeed.createCriteria().list(max: params.max, offset: params.offset)
	{
		and
		{
			if(params.sort && params.order) { order(params.sort, params.order)}
		}
	}
	
    def response = [:]
    response.feeds = results
	response.feedsTotal = results.totalCount
    response
  }
}
