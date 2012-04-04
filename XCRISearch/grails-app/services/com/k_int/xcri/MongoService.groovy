package com.k_int.xcri

import com.gmongo.GMongo

class MongoService {

  def mongo = null;

  @javax.annotation.PostConstruct
  def startup() {
    mongo = new com.gmongo.GMongo();
  }

  @javax.annotation.PreDestroy
  dev shutdown() {
    mongo.close()
  } 

  def getMongo() {
    mongo
  }
}
