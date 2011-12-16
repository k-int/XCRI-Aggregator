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
    running_feeds[feed_definition.id] = feed_definition;
    feed_definition.status=2
    feed_definition.save(flush:true)

    log.debug("Assemble http client to ${feed_definition.target.baseurl} - ${feed_definition.target.identity}/${feed_definition.target.credentials}");
    def aggregator_service = new HTTPBuilder( feed_definition.target.baseurl )
    aggregator_service.auth.basic feed_definition.target.identity, feed_definition.target.credentials

    if ( feed_definition instanceof SingleFileDatafeed ) {
      log.debug("Processing single file datafeed. ${feed_definition.baseurl} is the url of the resource");

      log.debug("Calling upload stream");
      java.net.URL resource = new java.net.URL(feed_definition.baseurl)
      def response = uploadStream(resource.openStream(),aggregator_service);
      log.debug("Assigning json response to database object");
      feed_definition.jsonResponse = response as JSON
    }

    feed_definition.status=3
    feed_definition.save(flush:true)

    log.debug("Removing feed from running_feeds map")
    running_feeds.remove(feed_definition.id)
  }

  def uploadStream(input_stream,target_service) {

    def upload_result = [:]

    log.debug("About to make post request");

    byte[] resource_to_deposit = input_stream.getBytes()

    log.debug("Length of input stream is ${resource_to_deposit.length}");

    target_service.request(POST) {request ->
      requestContentType = 'multipart/form-data'

      // Much help taken from http://evgenyg.wordpress.com/2010/05/01/uploading-files-multipart-post-apache/
      def multipart_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
      multipart_entity.addPart( "owner", new StringBody( "aa1", "text/plain", Charset.forName( "UTF-8" )))  // Owner
      multipart_entity.addPart( "upload", new org.apache.http.entity.mime.content.ByteArrayBody(resource_to_deposit, 'filename') )

      request.entity = multipart_entity;

      response.success = { resp, data ->
        log.debug("response status: ${resp.statusLine}")
        
        log.debug("Response data code: ${data?.code}");

        //data?.eventLog?.each {
        //  log.debug("log entry: ${it}");
        //}

        upload_result = data;

        // assert resp.statusLine.statusCode == 200
      }

      response.failure = { resp ->
        log.debug("Failure :( ${resp}");
        // assert resp.status >= 400
      }
    }
    log.debug("uploadStream completed");
    upload_result
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

