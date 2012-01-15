package com.k_int.xcri

import grails.converters.*

class SearchController {


  def ESWrapperService

  def index() { 
    // log.debug("Search Index, params.coursetitle=${params.coursetitle}, params.coursedescription=${params.coursedescription}, params.freetext=${params.freetext}")
    log.debug("Search Index, params.q=${params.q}")

    def pagename = 'index'
    def result = [:]

    // Get hold of some services we might use ;)
    def mongo = new com.gmongo.GMongo();
    def db = mongo.getDB("xcri")
    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    if ( params.q != null ) {

      def terms = params.q
	  params.max = Math.min(params.max ? params.int('max') : 10, 100)
	  params.offset = params.offset ? params.int('offset') : 0
	  
      if ( terms=='' )
        terms="*"

      def search = esclient.search {
        indices "courses"
        types "course"
        source {
          from = params.offset
          size = params.max
          query {
            query_string (query: terms)
          }
          facets [
            subject { 
              terms {
                field = 'subject'
              }
            }
            //   terms{
            //     field = "subject"
            //   }
            // }
            // provid { 
            //   terms(field:"provid") 
            // }
            // quallvl { 
            //   terms(field:"qual.level") 
            // }
          ]
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
      result.resultsTotal = search.response.hits.totalHits


      pagename='results'
      // render(view:'results',model:result) 
    }
    else {
      log.debug("No query.. Show search page")
      // render(view:'index',model:result)
    }

    withFormat {
      html {
        render(view:pagename,model:result)
      }
      xml {
        render result as XML
      }
      json {
        render result as JSON
      }
    }
  }
}
