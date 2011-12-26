package com.k_int.xcri

class CourseController {

  def index() { 
    log.debug("Course controller, params.id=${params.id}")

    def result = [:]

    // Get hold of some services we might use ;)
    def mongo = new com.gmongo.GMongo();
    def db = mongo.getDB("xcri")
    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    if ( params.id != null ) {
      // Form passed in a query

      def search = esclient.search {
        indices "courses"
        types "course"
        source {
          query {
            term(_id:params.id)
          }
        }
      }

      log.debug("Search returned $search.response.hits.totalHits total hits")
      log.debug("First hit course is $search.response.hits[0]")
      result.searchresult = search.response
      render(view:'results',model:result)
    }
    else {
      log.warn("No query.. Show search page")
      render(view:'notfound',model:result)
    }

  }
}
