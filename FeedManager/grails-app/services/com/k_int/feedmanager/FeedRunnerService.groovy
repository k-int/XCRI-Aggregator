package com.k_int.feedmanager

import com.k_int.feedmanager.*

class FeedRunnerService {

    def collect(feed_definition) {
      log.debug("Collecting ${feed_definition}");

      if ( feed_definition instanceof SingleFileDatafeed ) {
        log.debug("Processing single file datafeed. ${feed_definition.baseurl} is the url of the resource");
      }
    }
}
