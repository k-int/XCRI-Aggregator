package com.k_int.xcri

import grails.converters.*
import org.elasticsearch.groovy.common.xcontent.*
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat;

class OaiController {

  def ESWrapperService
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

  def index() { 
    org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
    org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()
    switch ( params.verb?.toUpperCase() ) {
      case 'GETRECORD':
        getRecord(params,esclient)
        break;
      case 'LISTRECORDS':
        listRecords(params,esclient)
        break;
      case 'LISTIDENTIFIERS':
        listIdentifiers(params,esclient)
        break;
      case 'IDENTIFY':
        identify()
        break;
      case 'LISTMETADATAFORMATS':
        listMetadataFormats()
        break;
      case 'LISTSETS':
        listSets()
        break;
      default:
        log.debug('Unhandled');
        break;
    }
  }

  def identify() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.setOmitEmptyAttributes(true);
    xml.setOmitNullAttributes(true);
    xml.'OAI-PMH'('xmlns':'http://www.openarchives.org/OAI/2.0/',
                  'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
                  'xsi:schemaLocation':'http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd') {
      responseDate(formatter.format(new Date()))
      request('verb':params.verb)
      Identify {
        repositoryName('XCRI Aggregator - OAI API')
        baseUrl('http://coursedata.k-int.com/discover/oai')
        protocolVersion('2.0')
      }
    }
    render(contentType:"application/xml", text: writer.toString())
  }

  def getRecord(params, esclient) {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.setOmitEmptyAttributes(true);
    xml.setOmitNullAttributes(true);
    xml.'OAI-PMH'('xmlns':'http://www.openarchives.org/OAI/2.0/',
                  'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
                  'xsi:schemaLocation':'http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd') {
      responseDate(formatter.format(new Date()))
      request('verb':params.verb,
              'metadataPrefix':params.metadataPrefix)
      GetRecord {
        header {
          identifier(hit.source._id)
          dateStamp(hit.source.lastModified)
        }
        metadata {
          course {
            recordAsXML(xml,hit.source)
          }
        }        
      }
    }
    render(contentType:"application/xml", text: writer.toString())
  }

  def listRecords(params, esclient) {
    log.debug('List Records');

    def writer = new StringWriter()

    def xml = new MarkupBuilder(writer)
    xml.setOmitEmptyAttributes(true);
    xml.setOmitNullAttributes(true);

    def resp;
    def last_rec = null;

    Long from = null;
    from = params.from != null ? new Long ( formatter.parse(params.from).getTime() ) : null;
    Long until = null;
    until = params.until != null ? new Long ( formatter.parse(params.to).getTime() ) : null;

    if ( params.resumptionToken != null ) {
      log.debug("Processing resumption token ${params.resumptionToken}");

      String[] components = params.resumptionToken.split(';');

      if ( components.length == 3 ) {
        from = new Long(Long.parseLong(components[1]))
        log.debug("From is ${from} components were ${components}");
      }
      else {
        log.error("Could not parse resumption token");
      }
      resp = findFor(from, null, esclient);
    }
    else {
      log.debug("Processing first request");
      // This OAI Service only supports the metadata prefix XCRICourseInstance
      from = processDate(params.from)
      until = processDate(params.from)
      resp = findFor(null, null, esclient);
    }

    xml.'OAI-PMH'('xmlns':'http://www.openarchives.org/OAI/2.0/',
                  'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
                  'xsi:schemaLocation':'http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd') {
      responseDate(formatter.format(new Date()))
      request('verb':params.verb,
              'from':params.from,
              'until':params.to,
              'set':params.set,
              'resumptionToken':params.resumptionToken,
              'metadataPrefix':params.metadataPrefix) {
      }
      listRecords {
        resp?.hits.each { hit ->
          record {
            header {
              identifier(hit.source._id)
              dateStamp(hit.source.lastModified)
            }
            metadata {
              course {
                recordAsXML(xml,hit.source)
              }
            }
          }
        }
      }
      if ( resp?.hits != null ) {
        if ( resp.hits.hits.length == 51 ) {
          resumptionToken("rt;${resp.hits.getAt(50).source.lastModified};${resp.hits.getAt(50).source._id}");
        }
      }
    }

    log.debug("Render...");
    render(contentType:"application/xml", text: writer.toString())
  }

  def listMetadataFormats() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.setOmitEmptyAttributes(true);
    xml.setOmitNullAttributes(true);
    xml.'OAI-PMH'('xmlns':'http://www.openarchives.org/OAI/2.0/',
                  'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
                  'xsi:schemaLocation':'http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd') {
      responseDate(formatter.format(new Date()))
      request('verb':params.verb)
      ListMetadataFormats {
        metadataFormat {
          metadataPrefix('course')
        }
      }
    }
    render(contentType:"application/xml", text: writer.toString())
  }

  def listSets() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    xml.setOmitEmptyAttributes(true);
    xml.setOmitNullAttributes(true);
    xml.'OAI-PMH'('xmlns':'http://www.openarchives.org/OAI/2.0/',
                  'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance',
                  'xsi:schemaLocation':'http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd') {
      responseDate(formatter.format(new Date()))
      request('verb':params.verb)
      ListSets {
      }
    }
    render(contentType:"application/xml", text: writer.toString())
  }

  def recordAsXML(builder, record) {
    log.debug("recordAsXML, ${record.getClass().getName()}");
    if ( record instanceof java.util.Map ) {
      record.each { ent ->
        if ( ent.value instanceof java.util.Map ) {
          if ( ent.value.size() > 0 ) {
            log.debug("1. ${ent.key}");
            builder."${ent.key}"(recordAsXML(builder, ent.value))
          }
        }
        else if ( ent.value instanceof List ) {
          ent.value.each { le ->
            log.debug("2. ${ent.key}");
            if ( ( ( le instanceof java.util.Map ) || ( le instanceof java.util.List ) ) && ( le.size() > 0 ) ) {
              builder."${ent.key}"() {
                recordAsXML(builder, le)
              }
            }
            else {
              builder."${ent.key}"(le.toString())
            }
          }
        }
        else {
          def str_value = ent.value.toString();
          def str_len = str_value.length()
          if ( str_len > 0 ) {
            log.debug("3. ${ent.key} \"${str_value}\" ${ent.value.getClass().getName()}");
            builder."${ent.key}"(str_value)
          }
        }
      }
    }
    else {
    }
  }

  def findFor(from, until, esclient) {

    log.debug("findFor ${from} ${until}..");
    StringWriter sw = new StringWriter();

    sw.write("*");

    if ( ( from != null ) && ( until == null ) ) {
      sw.write("AND lastModified:[${from} TO *]")
    }
    else if ( ( from == null ) && ( until != null ) ) {
      sw.write("AND lastModified:[* TO ${until}]")
    }
    else if ( ( from != null ) && ( until != null ) ) {
      sw.write("AND lastModified:[${from} TO ${until}]")
    }

    def qry = sw.toString();

    log.debug("Using query: ${qry}");

    def search = esclient.search{
      indices "courses"
      types "course"
      source {
        from = 0
        size = 51
        query {
          query_string (query: qry)
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

  // Convert datestr into ms since epoch
  def processDate(datestr) {
    Long result = null;
    if ( datestr != null ) {
      try {
        
      }
      catch ( Exception e ) {
        log.warn("Problem parsing date",e);
      }
      finally {
      }
    }
    result;
  }
}
