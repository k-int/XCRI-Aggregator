package com.k_int.feedmanager

class AggregationService {

  static belongsTo = [owner:ShiroUser]

  String baseurl

  String identity
  String credentials

  static constraints = {
  }

}
