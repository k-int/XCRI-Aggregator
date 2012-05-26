package xcrigrails

import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import groovyx.net.http.RESTClient
import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import static groovyx.net.http.Method.GET
import grails.converters.*

class SearchController {

  def es_endpoint = new RESTClient( 'http://coursedata.k-int.com:9200/' )

  def index() { 
    def result = [:]
    if ( params.coursename ) {
      result.hits = ESQry(params.coursename)
    }
    result
  }

  def ESQry(coursename) {

    def result = null;

    es_endpoint.request(GET, groovyx.net.http.ContentType.JSON) { request ->
      uri.path = 'courses/course/_search'
      uri.query = [
        q:"${coursename}"
      ]
      request.getParams().setParameter("http.socket.timeout", new Integer(10000))
      headers.Accept = 'application/json'

      response.success = { resp, json ->
        log.debug( "Server Response: ${resp.statusLine}" )
        log.debug( "Server Type: ${resp.getFirstHeader('Server')}" )
        log.debug( "content type: ${resp.headers.'Content-Type'}" )

        result = json
        // json.hits.hits.each { doc ->
        //   log.debug("Hit... ${doc}");
        // }
      }
    }
    result
  }

}
