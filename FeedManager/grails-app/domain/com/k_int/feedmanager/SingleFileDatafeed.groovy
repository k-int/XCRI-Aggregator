package com.k_int.feedmanager

class SingleFileDatafeed extends Datafeed {

    public SingleFileDatafeed() {
      feedtype = "SingleFile"
    }

    String baseurl

    // 1==new
    int status = 1

    static constraints = {
    }
}
