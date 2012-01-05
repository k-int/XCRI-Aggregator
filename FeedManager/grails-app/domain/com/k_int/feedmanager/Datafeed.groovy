package com.k_int.feedmanager

class Datafeed {

    static belongsTo = [owner:ShiroUser]

    AggregationService target

    String feedname
    String feedtype
    String statusMessage
    Long lastCheck
    Long checkInterval
    String jsonResponse
    boolean active
    String dataProvider
    String resourceIdentifier

    // Setting this should cause an explict charset conversion
    String sourceCharset

    // 1==new, 2==running, 3==OK, 4==Error, 5==Scheduled
    int status = 1

    static constraints = {
      jsonResponse(maxSize:1000000,nullable:true,blank:true)
      statusMessage(maxSize:512,nullable:true,blank:true)
      lastCheck(nullable:true)
      checkInterval(nullable:true)
      resourceIdentifier(nullable:true)
      sourceCharset(nullable:true)
    }
}
