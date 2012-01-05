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
    // ID to be used inside aggregator
    String dataProvider
    String resourceIdentifier
    String providerUrl
    String providerTechnicalContact
    String providerEmail
    String iconUrl
 

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
      providerUrl(nullable:true)
      providerTechnicalContact(nullable:true)
      providerEmail(nullable:true)
      iconUrl(nullable:true)
    }
}
