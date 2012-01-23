package com.k_int.xcri

import grails.converters.*
import org.elasticsearch.groovy.common.xcontent.*

class SearchController {

  def ESWrapperService

  // Map the parameter names we use in the webapp with the ES fields
  def reversemap = ['subject':'subject', 'provider':'provid']

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

      params.max = Math.min(params.max ? params.int('max') : 10, 100)
      params.offset = params.offset ? params.int('offset') : 0

      def query_str = buildQuery(params)
      log.debug("query: ${query_str}");

  /* old code 
   *   def flist = []

      if ( params.subject ) {
        flist.add(['field':'subject','value':params.subject])
      }*/
    
      def search_closure = {
        source {
          from = params.offset
          size = params.max
          query {
            query_string (query: query_str)
          }
          facets {
            subject {
              terms {
                field = 'subject'
              }
            }
            provider {
              terms {
                field = 'provid'
              }
            }
          }
        }
      }

      testSearchClosure(search_closure)

      def search = esclient.search(search_closure)
      //      and : [
      //        params.coursetitle ?: { term(title:params.coursetitle) },
      //        params.coursedescription ?: { term(descriptions:params.coursedescription) },
      //        params.freetext ?: { term(params.freetext) },
      //      ]

      println "Search returned $search.response.hits.totalHits total hits"
      println "First hit course is $search.response.hits[0]"
      result.searchresult = search.response
      result.resultsTotal = search.response.hits.totalHits
      result.facets = search.response.facets?.facets

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

  def testSearchClosure(c) {
    log.debug("testSearchClosure");
    def builder = new GXContentBuilder()
    def b = builder.buildAsString(c)
    log.debug(b.toString())
  }

  def buildQuery(params) {

    StringWriter sw = new StringWriter()

   if ( ( params != null ) && ( params.q != null ) )
     sw.write(params.q)
   else
     sw.write("*:*")

    reversemap.each { mapping ->

      log.debug("testing ${mapping.key}");

      if ( params[mapping.key] != null ) {
        if ( params[mapping.key].class.isArray() ) {
          params[mapping.key].each { p ->
            sw.write(" AND ")
            sw.write(mapping.value)
            sw.write(":")
            sw.write("\"${p}\"")
          }
        }
        else {
          sw.write(" AND ")
          sw.write(mapping.value)
          sw.write(":")
          sw.write("\"${params[mapping.key]}\"")
        }
      }
    }

    sw.toString()
  }
}
