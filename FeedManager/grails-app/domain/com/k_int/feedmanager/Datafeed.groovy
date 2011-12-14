package com.k_int.feedmanager

import ShiroUser

class Datafeed {

    static belongsTo = [owner:ShiroUser]

    AggregationService target

    String feedname
    String feedtype

    static constraints = {
    }

    
}
