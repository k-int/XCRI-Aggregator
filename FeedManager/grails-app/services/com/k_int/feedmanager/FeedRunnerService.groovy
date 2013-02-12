package com.k_int.feedmanager

import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import grails.converters.*
import java.security.MessageDigest

class FeedRunnerService {

  def ESWrapperService
  def ConfigService
  def running_feeds = [:]

  def collect(force_process, feed_definition_id) {

    log.debug("Attempting to Collect feed ${feed_definition_id}");

    if ( running_feeds.keySet().contains(feed_definition_id) ) {
      log.warn("Cannot start already running feed");
    }
    else {
      log.warn("Starting feed ${feed_definition_id}");
      doCollectFeed(force_process, feed_definition_id)
    }
  }

  def doCollectFeed(force_process, feed_definition_id) {

    log.debug("starting doCollectFeed ${feed_definition_id}");

    def feed_definition = com.k_int.feedmanager.SingleFileDatafeed.get(feed_definition_id)
    running_feeds[feed_definition_id] = feed_definition;
    feed_definition.status=2
    feed_definition.save(flush:true)

    try {
      log.debug("Assemble http client to ${feed_definition.target.baseurl} - ${feed_definition.target.identity}/${feed_definition.target.credentials} for feed ${feed_definition.baseurl}");
      def aggregator_service = new HTTPBuilder( feed_definition.target.baseurl )
      aggregator_service.auth.basic feed_definition.target.identity, feed_definition.target.credentials

      if ( feed_definition instanceof SingleFileDatafeed ) {
        log.debug("Processing single file datafeed. ${feed_definition.baseurl}");
        java.net.URL resource = new java.net.URL(feed_definition.baseurl)
        java.net.URLConnection url_conn = resource.openConnection();
        url_conn.setConnectTimeout(10000); // 10 seconds
        url_conn.setReadTimeout(1200000); // 20mins
        url_conn.connect();
        log.debug("url connection reports content encoding as : ${url_conn.getContentEncoding()}");
        def upload_result = uploadStream(force_process, url_conn.getInputStream(),aggregator_service,feed_definition)
        log.debug("Refresh feed definition ${feed_definition.id}");
        feed_definition.refresh()
        feed_definition.jsonResponse=upload_result.jsonResponse
        feed_definition.status = upload_result.status
        feed_definition.statusMessage=upload_result.statusMessage
        feed_definition.resourceIdentifier=upload_result.resourceIdentifier
        feed_definition.checksum=upload_result.checksum
        log.debug("after upload stream, resource identifier returned is ${upload_result.resourceIdentifier}");
        // feed_definition.publicationStatus=upload_result.publicationStatus
        // WTAF: feed_definition.save(flish:true)
        if ( feed_definition.save(flush:true) ) {
          log.debug("updated feed saved, result status value is ${upload_result.status}");
        }
        else {
          log.error("updated feed not saved, result status value is ${upload_result.status}");
        }

      }
      else {
        log.warn("Unhandled feed type");
      }
    }
    catch ( Exception e ) {
      log.error("Invalid URL or other exception: ${feed_definition.baseurl}",e)
      feed_definition.refresh()
      feed_definition.status=4
      feed_definition.statusMessage=e.message
      feed_definition.save(flush:true)
    }
    finally {
      feed_definition.refresh()
      feed_definition.lastCheck = System.currentTimeMillis()
      feed_definition.lastCollect = feed_definition.lastCheck
      feed_definition.forceHarvest = false
      feed_definition.save(flush:true)
      log.debug("Removing feed from running_feeds map")
      running_feeds.remove(feed_definition_id)
      
      //count records stored in elastic search
      log.debug("refresh total record count");
      feed_definition.totalRecords = getRecordCount(feed_definition.resourceIdentifier);   
      feed_definition.save(flush:true);
    }

    log.debug("Exiting doCollectFeed for feed ${feed_definition_id}");
  }

  def quickValidation(byte_array, feed_definition) {
    log.debug("Running quick validation checks");
    byte[] result = byte_array
    try {
      log.debug("Try and parse the byte array into XML.. detecting invalid encodings")
      def xml = new XmlSlurper().parse(new java.io.ByteArrayInputStream(byte_array))
      log.debug("Read bytes as UTF-8. Good!");
    }
    catch ( Exception e ) {
      log.error("Unable to parse bytes as XML.. Maybe there is a charset encoding issue. Trying ISO-8859-1 Instead",e);
      result = new String(byte_array,"ISO-8859-1").getBytes("UTF-8")

      // Set the source charset so we know for next time.
      feed_definition.sourceCharset = 'ISO-8859-1';
    }

    result
  }

  def uploadStream(force_process,input_stream,target_service,feed_definition) {

    def result = [:]
    result.jsonResponse = null;
    result.status=-1
    result.statusMessage=null;
    result.resourceIdentifier=null;
    result.checksum=null;
    result.publicationStatus=feed_definition.publicationStatus;

    log.debug("About to make post request");

    try {
      byte[] resource_to_deposit = null;
      if ( feed_definition.sourceCharset != null ) {
        log.debug("Feed has explicit character set conversion... Adapting");
        resource_to_deposit = new String(input_stream.getBytes(),feed_definition.sourceCharset).getBytes();
      }
      else {
        log.debug("No explicit conversion, continue with quick validation...");
        resource_to_deposit = quickValidation(input_stream.getBytes(), feed_definition)
        if ( resource_to_deposit ) {
          log.debug("Quick validation completed. have resource");
        }
        else {
          throw new RuntimeException("Attempting charset conversion failed...");
        }
      }

      // Compute MD5 Sum for resource
      MessageDigest md5_digest = MessageDigest.getInstance("MD5");
      md5_digest.update(resource_to_deposit,0,resource_to_deposit.length);
      byte[] md5sum = md5_digest.digest();
      String md5sumHex = new BigInteger(1, md5sum).toString(16);

      log.debug("Length of input stream is ${resource_to_deposit.length}, Checksum is ${md5sumHex}");

      if ( ( force_process ) ||
           ( feed_definition.publicationStatus == 1 ) ||
           ( feed_definition.publicationStatus == 3 ) ||
           ( feed_definition.checksum == null ) ||
           ( feed_definition.checksum != md5sumHex ) ) {

        // target_service.request(POST,JSON) {request ->
        target_service.request(POST) {request ->
          requestContentType = 'multipart/form-data'

          // Much help taken from http://evgenyg.wordpress.com/2010/05/01/uploading-files-multipart-post-apache/
          def multipart_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
          multipart_entity.addPart( "owner", new StringBody( feed_definition.dataProvider, "text/plain", Charset.forName( "UTF-8" )))  // Owner

          switch ( feed_definition.publicationStatus ) {
            case 0:
              log.debug("Feed publication status == private");
              multipart_entity.addPart( "ulparam_feedStatus", new StringBody( "private", "text/plain", Charset.forName( "UTF-8" )))
              multipart_entity.addPart( "ulparam_force", new StringBody( "${force_process}", "text/plain", Charset.forName( "UTF-8" )))
              break;
            case 1:
              log.debug("Publish");
              multipart_entity.addPart( "ulparam_feedStatus", new StringBody( "public", "text/plain", Charset.forName( "UTF-8" )))
              multipart_entity.addPart( "ulparam_force", new StringBody( "true", "text/plain", Charset.forName( "UTF-8" )))
              result.publicationStatus = 2
              break;
            case 2:
              log.debug("Feed publication status == public");
              multipart_entity.addPart( "ulparam_force", new StringBody( "${force_process}", "text/plain", Charset.forName( "UTF-8" )))
              multipart_entity.addPart( "ulparam_feedStatus", new StringBody( "public", "text/plain", Charset.forName( "UTF-8" )))
              break;
            case 3:
              log.debug("Unpublish");
              multipart_entity.addPart( "ulparam_feedStatus", new StringBody( "private", "text/plain", Charset.forName( "UTF-8" )))
              multipart_entity.addPart( "ulparam_force", new StringBody( "true", "text/plain", Charset.forName( "UTF-8" )))
              result.publicationStatus = 0
              break;
            default:
              log.warn("Unhandled feed publication status ${feed_definition.publicationStatus}");
              break;
          }

          def uploaded_file_body_part = new org.apache.http.entity.mime.content.ByteArrayBody(resource_to_deposit, 'text/xml', 'filename')
          multipart_entity.addPart( "upload", uploaded_file_body_part)

          request.entity = multipart_entity;
          
          response.success = { resp, data ->
            log.debug("response data: ${resp}")
            log.debug("response status: ${resp.statusLine}")
            log.debug("Assigning json response to database object");
            result.jsonResponse = data as JSON
            result.status=3
            result.statusMessage="Deposit:OK / Code:${data?.code} / Status:${data.status} / Message:${data.message}";
            if ( ( data.resource_identifier != null ) && ( data.resource_identifier.length() > 0 ) ) {
              result.resourceIdentifier=data.resource_identifier
            }
            else {
              log.error("Handler seems not to have returned a resource identifier");
            }
            result.checksum = md5sumHex
            // assert resp.statusLine.statusCode == 200
          }

          response.failure = { resp ->
            log.error("Failure - ${resp} status:${resp.status}");
            result.status=4
            result.statusMessage="Error uploading to aggregator. ${resp.status}. N.B. Any status code here refers to the aggregator, and not the XCRI-CAP source URL"
            // assert resp.status >= 400
          }
        }
      }
      else {
        log.debug("No action, checksum match");
        //no action so reset to successful completion
        result.status=3
      }
    }
    catch ( Exception e ) {
      log.error("Unexpected exception trying to read remote stream",e)
      result.status=4
      result.statusMessage="Error trying to read feed: ${e.message}"
    }
    finally {
      log.debug("uploadStream try block completed");
    }

    log.debug("uploadStream completed. result is ${result}");
    result
  }

  def getDatafeed(id) {
    log.debug("Looking up data feed ${id}");
    def result = running_feeds[id]
    if ( result == null ) {
      log.debug("feed not currently active, get from db");
      result = Datafeed.get(id)
    }

    result
  }
  
  def getRecordCount(provid) {

      log.debug("getRecordCount for providerId:\"${provid}\"");

      org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
      org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()
      
      def search = esclient.count{
          indices "priv_courses"
          types "course"
          query {
              query_string (query: "provid:${provid}")
          }
      }

     // log.debug("Result of count: ${search}");
     // log.debug("Result of count: ${search.response}");
     log.debug("Result of count: ${search.response.count}");
 
     return search.response.count
  }
}

