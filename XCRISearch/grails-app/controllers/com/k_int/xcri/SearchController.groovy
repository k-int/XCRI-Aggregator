package com.k_int.xcri

import grails.converters.*
import org.elasticsearch.groovy.common.xcontent.*
import groovy.xml.MarkupBuilder


class SearchController {

  def ESWrapperService

  // Map the parameter names we use in the webapp with the ES fields
  def reversemap = ['subject':'subject', 'provider':'provid', 'studyMode':'presentations.studyMode','qualification':'qual.type']
  
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

    //get search config for advanced select boxes
    result.search_config = grailsApplication.config.search
    
    if ( params.q && params.q.length() > 0) {

      params.max = Math.min(params.max ? params.int('max') : 10, 100)
      params.offset = params.offset ? params.int('offset') : 0

      //def params_set=params.entrySet()
      
      if(!params.subject)  params.subject = []
      else
      {
          params.subject = [params.subject].flatten()
      }
      
      if(!params.provider) params.provider = []
      else
      {
          params.provider = [params.provider].flatten()
      } 
            
      def query_str = buildQuery(params)
      log.debug("query: ${query_str}");
          
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
            provider
            {
                terms {
                     field = 'provid'
                }
            }
          }
        }
      }

      testSearchClosure(search_closure)

      def search = esclient.search(search_closure)

      println "Search returned $search.response.hits.totalHits total hits"
      println "First hit course is $search.response.hits[0]"
      result.hits = search.response.hits
      result.resultsTotal = search.response.hits.totalHits
      // result.facets = search.response.facets?.facets

      // We pre-process the facet response to work around some translation issues in ES
      if ( search.response.facets != null ) {
        result.facets = [:]
        search.response.facets.facets.each { facet ->
          def facet_values = []
          facet.value.entries.each { fe ->
              
            log.debug('adding to '+ facet.key + ': ' + fe.term + ' (' + fe.count + ' )')
            facet_values.add([term: fe.term,count:"${fe.count}"])
          }
          
          result.facets[facet.key] = facet_values
        }
      }

      pagename='results'
      // render(view:'results',model:result) 
    }
    else {
      log.debug("No query.. Show search page")
      // render(view:'index',model:result)
    }

    withFormat {
      rss {
        renderRSSResponse(result)
      }
      atom {
        renderATOMResponse( result,params.max )
      }
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
        if ( params[mapping.key].class == java.util.ArrayList) {
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

  def renderRSSResponse(results) {

    def output_elements = buildOutputElements(results.hits)

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.rss(version: '2.0') {
      channel {
        title("Open Family Services RSS Response")
        description("Open Family Services RSS Description")
        copyright("(c) Open Family Services")
        "opensearch:totalResults"(results.hits.totalHits)
        // "opensearch:startIndex"(results.search_results.results.start)
        "opensearch:itemsPerPage"(10)
        output_elements.each { i ->  // For each record
          entry {
            i.each { tuple ->   // For each tuple in the record
              "${tuple[0]}"("${tuple[1]}")
            }
          }
        }
      }
    }

    render(contentType:"application/rss+xml", text: writer.toString())
  }


  def renderATOMResponse(results,hpp) {

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    def output_elements = buildOutputElements(results.hits)

    xml.feed(xmlns:'http://www.w3.org/2005/Atom') {
        // add the top level information about this feed.
        title("Open Family Services ATOM Response")
        description("Open Family Services ATOM Response")
        copyright("(c) Open Family Services")
        "opensearch:totalResults"(results.hits.totalHits)
        // "opensearch:startIndex"(results.search_results.results.start)
        "opensearch:itemsPerPage"("${hpp}")
        // subtitle("Serving up my content")
        //id("uri:uuid:xxx-xxx-xxx-xxx")
        link(href:"http://coursedata.k-int.com")
        author {
          name("XCRI-DCAP")
        }
        //updated sdf.format(new Date());

        // for each entry we need to create an entry element
        output_elements.each { i ->
          entry {
            i.each { tuple ->
                "${tuple[0]}"("${tuple[1]}")
            }
          }
        }
    }

    render(contentType:'application/xtom+xml', text: writer.toString())
  }

  def buildOutputElements(searchresults) {
    // Result is an array of result elements
    def result = []

    searchresults.hits.each { doc ->
      log.debug("adding ${doc} ${doc.source.title}");
      def docinfo = [];

      docinfo.add(['dc.title',doc.source.title])
      docinfo.add(['dc.description',doc.source.description])
      docinfo.add(['dc.identifier',doc.source.title])
      // addField("dc.title", "dc.title", doc, docinfo)
      // addField("dc.description", "dc.description", doc, docinfo)
      // addField("dc.description", "dc.description", doc, docinfo)
      // addField("dc.identifier", "guid", doc, docinfo)
      // addField("modified", "pubdate", doc, docinfo)
      // docinfo.add(["link","${ApplicationHolder.application.config.ofs.pub.baseurl}/ofs/directory/${doc['authority_shortcode']}/${doc['aggregator.internal.id']}"])
      // if ( ( doc['lat'] != null ) && ( doc['lng'] != null ) ) {
      //   docinfo.add(["georss:point","${doc['lat']} ${doc['lng']}"])
      // }
      result.add(docinfo)
    }
    // println "Result ${result}"
    result
  }

  def count = {
      
      log.debug("in count method");
    def result = [:]
    // Get hold of some services we might use ;)
    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    
    if ( params.q && params.q.length() > 0)
    {
        def query_str = buildQuery(params)
        log.debug("count query: ${query_str}");
               
        def search = esclient.count{
            query {
               query_string (query: query_str)
            }
        }

        
        result.hits = search.response.count
    }
    
    render result as JSON
  }
}
