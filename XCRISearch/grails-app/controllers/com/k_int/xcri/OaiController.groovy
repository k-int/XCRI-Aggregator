package com.k_int.xcri

import grails.converters.*
import org.elasticsearch.groovy.common.xcontent.*
import groovy.xml.MarkupBuilder

class OaiController {

  def ESWrapperService

  def index() { 

    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    switch ( params.verb ) {
      case 'listRecords':
        listRecords(params,esclient)
        break;
      default:
        log.debug('Unhandled');
        break;
    }
  }

  def listRecords(params, esclient) {
    log.debug('List Records');

    def writer = new StringWriter()

    def xml = new MarkupBuilder(writer)
    xml.setOmitEmptyAttributes(true);
    xml.setOmitNullAttributes(true);

    if ( params.resumptionToken ) {
      log.debug("Processing resumption token");
    }
    else {
      log.debug("Processing first request");
      // This OAI Service only supports the metadata prefix XCRICourseInstance

    }

    def resp = findFor(null, null, esclient);
    resp.hits.each { hit ->
      log.debug("Processing a hit...");
      log.debug("${hit.source.
    }

    

    xml.'OAI-PMH'('xmlns':'http://www.openarchives.org/OAI/2.0/',
                  'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
                  'xsi:schemaLocation':'http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd') {
      responseDate('today')
      request('verb':params.verb,
              'from':params.from,
              'until':params.to,
              'set':params.set,
              'resumptionToken':params.resumptionToken,
              'metadataPrefix':params.metadataPrefix) {
      }
    }

    log.debug("Render...");
    render(contentType:"application/xml", text: writer.toString())
  }

  def findFor(from, until, esclient) {

    log.debug("findFor ${from} ${until}..");

    def search = esclient.search{
      indices "courses"
      types "course"
      source {
        from = 0
        size = 50
        query {
          query_string (query: '*')
        }
        sort = [
          [ 'lastModified' : [ 'order' : 'asc' ] ],
          [ '_id' : [ 'order' : 'asc' ] ]
        ]
      }
    }

    log.debug("search complete, found ${search.response.hits.totalHits} records");

    search.response
  }
}
