package com.k_int.xcri

class SearchController {


  def ESWrapperService

  def index() { 
    // log.debug("Search Index, params.coursetitle=${params.coursetitle}, params.coursedescription=${params.coursedescription}, params.freetext=${params.freetext}")
    log.debug("Search Index, params.q=${params.q}")

    def result = [:]

    // Get hold of some services we might use ;)
    def mongo = new com.gmongo.GMongo();
    def db = mongo.getDB("xcri")
    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    if ( params.q != null ) {
      def search = esclient.search {
        indices "courses"
        types "course"
        source {
          query {
            query_string ( query: params.q )
          }
        }
      }
      //      and : [
      //        params.coursetitle ?: { term(title:params.coursetitle) },
      //        params.coursedescription ?: { term(descriptions:params.coursedescription) },
      //        params.freetext ?: { term(params.freetext) },
      //      ]

      println "Search returned $search.response.hits.totalHits total hits"
      println "First hit course is $search.response.hits[0]"
      result.searchresult = search.response
      render(view:'results',model:result) 
    }
    else {
      log.debug("No query.. Show search page")
      render(view:'index',model:result)
    }
  }
}
