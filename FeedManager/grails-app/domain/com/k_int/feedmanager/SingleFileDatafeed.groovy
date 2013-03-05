package com.k_int.feedmanager

class SingleFileDatafeed extends Datafeed {

    public SingleFileDatafeed() {
        feedtype = "SingleFile"
    }

    String baseurl
    String checksum

    static constraints = {
        baseurl(blank:false,nullable:false)
        checksum(blank:false,nullable:true)
    }
}
