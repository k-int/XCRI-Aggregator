package com.k_int.xcri

import com.gmongo.GMongo

class MongoService {

    def mongo = null;

    @javax.annotation.PostConstruct
    def startup() {
        def options = new com.mongodb.MongoOptions()
        options.socketKeepAlive = true
        options.autoConnectRetry = true
        options.slaveOk = true
        mongo = new com.gmongo.GMongo('127.0.0.1', options);
        // mongo = new com.gmongo.GMongo();
    }

    @javax.annotation.PreDestroy
    def shutdown() {
        mongo.close()
    } 

    def getMongo() {
        mongo
    }
}
