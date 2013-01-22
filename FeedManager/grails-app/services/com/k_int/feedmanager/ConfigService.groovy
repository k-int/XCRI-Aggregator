package com.k_int.feedmanager

import org.codehaus.groovy.grails.commons.ApplicationHolder

@GrabResolver(name='es', root='https://oss.sonatype.org/content/repositories/releases')

@Grab(group='com.gmongo', module='gmongo', version='0.9.2')
@Grab(group='org.elasticsearch', module='elasticsearch-lang-groovy', version='1.1.0')

import com.gmongo.GMongo

class ConfigService
{
    static transactional = false
    def ESWrapperService
    
    @javax.annotation.PostConstruct
    def init() {
        
        log.debug("Initialising ES index & mappings");
            
        org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
        org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()
       
        // Get hold of an index admin client
        org.elasticsearch.groovy.client.GIndicesAdminClient index_admin_client = new org.elasticsearch.groovy.client.GIndicesAdminClient(esclient);
    
        // Create an index if none exists
        def future = index_admin_client.create {
          index 'courses'
        }
    
        // use http://localhost:9200/_all/_mapping to list all installed mappings
    
        // Declare a mapping of type "course" that explains to ES how it should index course elements
        log.debug("Attempting to put a mapping for course...");
        future = index_admin_client.putMapping {
          indices 'courses'
          type 'course'
          source {
            course {       // Think this is the name of the mapping within the type
              properties {
                provid {
                  type = 'string'
                  store = 'yes'
                  index = 'not_analyzed'
                }
                title { // We declare a multi_field mapping so we can have a default "title" search with stemming, and an untouched title via origtitle
                  type = 'multi_field'
                  fields {
                    title {
                      type = 'string'
                      analyzer = 'snowball'
                    }
                    origtitle {
                      type = 'string'
                      store = 'yes'
                    }
                  }
                }
                subject {
                  type = 'multi_field'
                  fields {
                    subject {
                      type = 'string'
                      store = 'yes'
                      index = 'not_analyzed'
                    }
                    subjectKw {
                      type = 'string'
                      analyzer = 'snowball'
                    }
                  }
                }
                provloc {
                  type = 'geo_point'
                }
                geoCounty {
                  type = 'string'
                  index = 'not_analyzed'
                }
                level {
                  type = 'string'
                  index = 'not_analyzed'
                }
                studyMode {
                  type = 'string'
                  index = 'not_analyzed'
                }
                qual {
                  properties {
                    level {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                  }
                }
                presentations {
                  properties {
                    start {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    startText {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    end {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    endText {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    applyTo {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    applyToText {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    enquireTo {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    enquireToText {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    attendanceMode {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                    studyMode {
                      type = 'string'
                      index = 'not_analyzed'
                    }
                  }
                }
              }
            }
          }
        }
        log.debug("Installed course mapping ${future}");
        
        log.debug("Initialising Mongo");
        
        // Get hold of mongodb
        def mongo = new com.gmongo.GMongo();
        def db = mongo.getDB("oda")
        
        // Store a definition of the searchable part of the resource in mongo
        def courses_aggregation = db.aggregations.findOne(identifier: 'uri:aggr:cld:courses')
    
        if ( courses_aggregation == null ) {
          // Create a definition of a course CLD
          courses_aggregation = [:]
        }
    
        courses_aggregation.identifier = 'uri:aggr:cld:courses'
        courses_aggregation.type = 'es'
        courses_aggregation.indexes = ['courses']
        courses_aggregation.types = ['course']
        courses_aggregation.title = 'All UK Courses'
        courses_aggregation.description = 'An searchable aggregation of course descriptions from institutions in the UK'
        courses_aggregation.access_points = [
                                        [ field:'identifier', label:'Identifier' ],
                                        [ field:'title', label:'Title' ],
                                        [ field:'descriptions', label:'Description' ] ]
        db.aggregations.save(courses_aggregation);
        
        log.debug("Finished Initialising Mongo");
  
    }
}
