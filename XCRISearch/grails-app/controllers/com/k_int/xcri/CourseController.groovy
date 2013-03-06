package com.k_int.xcri

import grails.converters.*

class CourseController {

    def ESWrapperService
    def mongoService

    def index() { 
        log.debug("Course controller, params.id=${params.id}")

        def result = [:]

        // Get hold of some services we might use ;)
        def db = mongoService.getMongo().getDB("xcri")
        org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
        org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

        try {
            log.debug("Look up course with id ${params.id}");
  
            if ( params.id != null ) {
                // Form passed in a query
                log.debug("Do lookup on course code ${params.id}");
  
  
                def course = esclient.get {
                    index "priv_courses"
                    type "course"
                    id "${params.id}"
                }
  
                if ( course?.response?.source ) {
                    result.course = course.response
                }
                else {
                    log.error("No record found");
                    render(view:'notfound',model:result)
                }
  
                //result
        
                withFormat {
                    html {
                        result
                    }
                    xml {
                        render result as XML
                    }
                    json {
                        render result as JSON
                    }
                }
            }
            else {
                log.warn("No query.. Show search page")
                render(view:'notfound',model:result)
            }
  
        }
        catch ( Exception e ) 
        {
            log.warn(e)
        }
        finally 
        {
        }
    }
}
