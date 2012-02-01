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

  def running_feeds = [:]

  def collect(feed_definition_id) {

    log.debug("Attempting to Collect feed ${feed_definition_id}");

    if ( running_feeds.keySet().contains(feed_definition_id) ) {
      log.warn("Cannot start already running feed");
    }
    else {
      log.warn("Starting feed ${feed_definition_id}");
      doCollectFeed(feed_definition_id)
    }
  }

  def doCollectFeed(feed_definition_id) {
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
        url_conn.connect();
        log.debug("url connection reports content encoding as : ${url_conn.getContentEncoding()}");
        uploadStream(url_conn.getInputStream(),aggregator_service,feed_definition)
      }
      else {
        log.warn("Unhandled feed type");
      }
    }
    catch ( Exception e ) {
      log.warn("Unhandled Exception trying to collect feed",e)
      feed_definition.status=4
      feed_definition.statusMessage=e.message
    }
    finally {
      feed_definition.lastCheck = System.currentTimeMillis()
      feed_definition.save(flush:true)
      log.debug("Removing feed from running_feeds map")
      running_feeds.remove(feed_definition_id)
    }
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
      log.debug("Unable to parse bytes as XML.. Maybe there is a charset encoding issue. Trying ISO-8859-1 Instead");
      byte_array = new String(byte_array,"ISO-8859-1").getBytes("UTF-8")

      // Set the source charset so we know for next time.
      feed_definition.sourceCharset = 'ISO-8859-1';
    }

    byte_array
  }

  def uploadStream(input_stream,target_service,feed_definition) {

    log.debug("About to make post request");

    try {
      byte[] resource_to_deposit = null;
      if ( feed_definition.sourceCharset != null ) {
        log.debug("Feed has explicit character set conversion... Adapting");
        resource_to_deposit = new String(input_stream.getBytes(),feed_definition.sourceCharset).getBytes();
      }
      else {
        log.debug("No explicit conversion, continue...");
        resource_to_deposit = quickValidation(input_stream.getBytes(), feed_definition)
      }

      // Compute MD5 Sum for resource
      MessageDigest md5_digest = MessageDigest.getInstance("MD5");
      md5_digest.update(resource_to_deposit,0,resource_to_deposit.length);
      byte[] md5sum = md5_digest.digest();
      String md5sumHex = new BigInteger(1, md5sum).toString(16);

      log.debug("Length of input stream is ${resource_to_deposit.length}, Checksum is ${md5sumHex}");

      if ( ( feed_definition.checksum == null ) ||
           ( feed_definition.checksum != md5sumHex ) ) {

        target_service.request(POST) {request ->
          requestContentType = 'multipart/form-data'

          // Much help taken from http://evgenyg.wordpress.com/2010/05/01/uploading-files-multipart-post-apache/
          def multipart_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
          multipart_entity.addPart( "owner", new StringBody( feed_definition.dataProvider, "text/plain", Charset.forName( "UTF-8" )))  // Owner

          def uploaded_file_body_part = new org.apache.http.entity.mime.content.ByteArrayBody(resource_to_deposit, 'text/xml', 'filename')
          multipart_entity.addPart( "upload", uploaded_file_body_part)

          request.entity = multipart_entity;
          
          response.success = { resp, data ->
            log.debug("response status: ${resp.statusLine}")
            log.debug("Response data code: ${data?.code}");
            log.debug("Assigning json response to database object");
            feed_definition.jsonResponse = data as JSON
            feed_definition.status=3
            feed_definition.statusMessage="Deposit:OK - code:${data?.code} / status:${data.status} / message:${data.message}";
            if ( ( data.resource_identifier != null ) && ( data.resource_identifier.length() > 0 ) )
              feed_definition.resourceIdentifier=data.resource_identifier
            feed_definition.checksum = md5sumHex
            // assert resp.statusLine.statusCode == 200
          }

          response.failure = { resp ->
            log.error("Failure - ${resp}");
            feed_definition.status=4
            feed_definition.statusMessage="Error uploading to aggregator. ${resp.status}. N.B. Any status code here refers to the aggregator, and not the XCRI-CAP source URL"
            // assert resp.status >= 400
          }
        }
      }
      else {
        log.debug("No action, checksum match");
        //no action so reset to successful completion
        feed_definition.status=3
      }
    }
    catch ( Exception e ) {
      log.error("Unexpected exception trying to read remote stream",e)
      feed_definition.status=4
      feed_definition.statusMessage="Error trying to read feed: ${e.message}"
    }
    finally {
      log.debug("uploadStream try block completed");
      feed_definition.save(flush:true);
    }
    log.debug("uploadStream completed");
  }

  def getDatafeed(id) {
    log.debug("Looking up data feed ${id}");
    def result = running_feeds[id]
    if ( result == null ) {
      log.debug("Looking up feed from db");
      result = Datafeed.get(id)
    }

    result
  }
}

