package com.k_int.feedmanager

import ShiroUser

class Datafeed {

    static belongsTo = [owner:ShiroUser]

    AggregationService target

    String feedname
    String feedtype

    String jsonResponse

    // 1==new, 2==running, 3==OK, 4==Error, 5==Scheduled
    int status = 1

    static constraints = {
      jsonResponse(maxSize:1000000,nullable:true,blank:true)
    }

    
}
