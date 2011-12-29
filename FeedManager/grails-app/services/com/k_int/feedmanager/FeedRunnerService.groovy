package com.k_int.feedmanager

import com.k_int.feedmanager.*

import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import grails.converters.*

class FeedRunnerService {

  def running_feeds = [:]

  def collect(feed_definition) {

    log.debug("Attempting to Collect feed ${feed_definition.id}");

    if ( running_feeds.keySet().contains(feed_definition.id) ) {
      log.warn("Cannot start already running feed");
    }
    else {
      log.warn("Starting feed ${feed_definition.id}");
      doCollectFeed(feed_definition)
    }
  }

  def doCollectFeed(feed_definition) {
    log.debug("starting doCollectFeed ${feed_definition.id}");
    running_feeds[feed_definition.id] = feed_definition;
    feed_definition.status=2
    feed_definition.save(flush:true)

    try {
      log.debug("Assemble http client to ${feed_definition.target.baseurl} - ${feed_definition.target.identity}/${feed_definition.target.credentials} for feed ${feed_definition.baseurl}");
      def aggregator_service = new HTTPBuilder( feed_definition.target.baseurl )
      aggregator_service.auth.basic feed_definition.target.identity, feed_definition.target.credentials

      if ( feed_definition instanceof SingleFileDatafeed ) {
        log.debug("Processing single file datafeed. ${feed_definition.baseurl}");
        java.net.URL resource = new java.net.URL(feed_definition.baseurl)
        uploadStream(resource.openStream(),aggregator_service,feed_definition)
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

    feed_definition.lastCheck = System.currentTimeMillis()
    feed_definition.save(flush:true)

    log.debug("Removing feed from running_feeds map")
    running_feeds.remove(feed_definition.id)
  }

  def uploadStream(input_stream,target_service,feed_definition) {

    log.debug("About to make post request");

    try {
      byte[] resource_to_deposit = input_stream.getBytes()

      log.debug("Length of input stream is ${resource_to_deposit.length}");

      target_service.request(POST) {request ->
        requestContentType = 'multipart/form-data'

        // Much help taken from http://evgenyg.wordpress.com/2010/05/01/uploading-files-multipart-post-apache/
        def multipart_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipart_entity.addPart( "owner", new StringBody( feed_definition.dataProvider, "text/plain", Charset.forName( "UTF-8" )))  // Owner
        multipart_entity.addPart( "upload", new org.apache.http.entity.mime.content.ByteArrayBody(resource_to_deposit, 'filename') )

        request.entity = multipart_entity;

        response.success = { resp, data ->
          log.debug("response status: ${resp.statusLine}")
          log.debug("Response data code: ${data?.code}");
          log.debug("Assigning json response to database object");
          feed_definition.jsonResponse = data as JSON
          feed_definition.status=3
          feed_definition.statusMessage="Deposit:OK - code:${data?.code} / status:${data.status} / message:${data.message}";
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
    catch ( Exception e ) {
      log.error("Unexpected exception trying to read remote stream",e)
      feed_definition.status=4
      feed_definition.statusMessage="Error trying to read feed: ${e.message}"
    }
    finally {
      log.debug("uploadStream try block completed");
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

