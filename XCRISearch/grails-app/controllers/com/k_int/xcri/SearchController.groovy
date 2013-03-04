package com.k_int.xcri

import grails.converters.*
import org.elasticsearch.groovy.common.xcontent.*
import groovy.xml.MarkupBuilder
import com.gmongo.GMongo
import java.text.SimpleDateFormat
import java.text.DateFormat


class SearchController {

    def ESWrapperService
    def gazetteerService
    def mongoService

    // Map the parameter names we use in the webapp with the ES fields
    def reversemap = ['subject':'subjectKw', 
                    'provider':'provid', 
                    'studyMode':'presentations.studyMode',
                    'qualification':'qual.type',
                    'level':'qual.level' ]
    def non_analyzed_fields = ['provid','subject','subject.subject','level','studyMode','qual.level','presentations.start','presentations.startText','presentations.studyMode','presentations.end','presentations.endText','presentations.applyTo','presentations.applyToText','presentations.enquireTo','presentations.enquireToText']
  
    def index() { 
        // log.debug("Search Index, params.coursetitle=${params.coursetitle}, params.coursedescription=${params.coursedescription}, params.freetext=${params.freetext}")
        log.debug("Search Index, params.q=${params.q}, format=${params.format}")

        def pagename = 'index'
        def result = [:]

        // Get hold of some services we might use ;)
        def db = mongoService.getMongo().getDB("xcri")
        org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
        org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

        def dunit = "miles"
        if ( params.dunit == 'km' ) {
            dunit = "km"
        }

        try {
            //get search config for advanced select boxes
            result.search_config = grailsApplication.config.search
      
            result.search_config.provider = list_providers()
      
            if ( params.q && params.q.length() > 0) {
  
                params.max = Math.min(params.max ? params.int('max') : 10, 100)
                params.offset = params.offset ? params.int('offset') : 0
                
                updateEsActivities(params.q);

                //def params_set=params.entrySet()
        
                if(!params.subject) {
                    params.subject = []
                }
                else {
                    params.subject = [params.subject].flatten()
                }
        
                if(!params.provider) {
                    params.provider = []
                }
                else {
                    params.provider = [params.provider].flatten()
                } 
        
                if(!params.level) { 
                    params.level = []
                }
                else {
                    params.level = [params.level].flatten()
                }
              
                def query_str = buildQuery(params)
                log.debug("query: ${query_str}");
          
                def geo = false;
                def g_lat = null;
                def g_lon = null;
                if (params.order && params.order.equalsIgnoreCase('distance') && params.location != null && params.location.length() > 0 ) {
                    // log.debug("Geocoding")
                    def gaz_resp = gazetteerService.resolvePlaceName(params.location)
                    if ( gaz_resp.places?.size() > 0 ) {
                        if ( gaz_resp.places[0] != null ) {
                            g_lat = gaz_resp.places[0].lat
                            g_lon = gaz_resp.places[0].lon
                            result.place = true
                            result.fqn =  gaz_resp.places[0].fqn
                            geo = true;
                            log.debug("Using lat/lon ${g_lat},${g_lon} distance unit is ${dunit}");
                        }
                        else {
                            result.flash = [:]
                            result.flash.message = "Something went wrong with the Geocoding, is your Postcode correct?"
                            log.error("Something badly wrong with geocoding");
                        }
                    }
                    else
                    {
                        result.flash = [:]
                        result.flash.message = "Postcode was not recognised"
                    }
                }
                else {
                    log.debug("No spatial");
                }
  
                def filters = geo;  // For adding more filters later.
            
                def search = esclient.search{
                    indices "courses"
                    types "course"
                    source {
                        from = params.offset
                        size = params.max
                        if ( geo == true ) {
                            sort = [
                  '_geo_distance' : [
                    'provloc' : [
                      'lat':"${g_lat}",
                      'lon':"${g_lon}"
                                    ],
                    'order' : 'asc',
                    'unit' : dunit
                                ]
                            ]
                        }
                        if ( filters == true ) {
                            query {
                                filtered {
                                    query {
                                        query_string (query: query_str)
                                    }
                                    filter {
                                        geo_distance {
                                            distance = "${params.distance}${dunit}"
                                            provloc {
                                                lat=g_lat
                                                lon=g_lon
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            query {
                                query_string (query: query_str)
                            }
                        }
                        facets {
                            subject {
                                terms {
                                    field = 'subject'
                                }
                            }
                            provider
                            {
                                terms {
                                    field = 'provid'
                                }
                            }
                            level
                            {
                                terms {
                                    field = 'qual.level'
                                }
                            }
                        }
                    }
                }
  
                /* It's possible to add the distance to the result even if not sorting by geo dist with
                script_fields {
                distance {
                params {
                lat=g_lat
                lon=g_lon
                }
                script="doc[\u0027provloc\u0027].distanceInKm(lat,lon)"
                }
                }
                 */
  
                // println "Search returned $search.response.hits.totalHits total hits"
                // println "First hit course is $search.response.hits[0]"
                result.hits = search.response.hits
        
                if(search.response.hits.maxScore == Float.NaN)//we cannot parse NaN to json so set to zero...
                {
                    search.response.hits.maxScore = 0;
                }
        
                result.resultsTotal = search.response.hits.totalHits

                // We pre-process the facet response to work around some translation issues in ES
                if ( search.response.facets != null ) {
                    result.facets = [:]
                    search.response.facets.facets.each { facet ->
                        def facet_values = []
                        facet.value.entries.each { fe ->
                
                            // log.debug('adding to '+ facet.key + ': ' + fe.term + ' (' + fe.count + ' )')
  
                            if ( facet.key == 'provider' ) {
                                def term = resolveTermIdentifier(fe.term)
                                if ( term != null ) {
                                    facet_values.add([term: fe.term,display:term.label,count:"${fe?.count}"])
                                }
                                else {
                                    facet_values.add([term: fe.term,display:fe.term,count:"${fe?.count}"])
                                }
                            }
                            else {
                                facet_values.add([term: fe.term,display:fe.term,count:"${fe?.count}"])
                            }
  
                        }
            
                        result.facets[facet.key] = facet_values
                    }
                }
  
                pagename='results'
                // render(view:'results',model:result) 
            }
            else {
                log.debug("No query.. Show search page")
                // render(view:'index',model:result)
            }
        }
        finally {
            try {
            }
            catch ( Exception e ) {
                log.error("problem",e);
            }
        }



        withFormat {
            html {
                render(view:pagename,model:result)
            }
            rss {
                renderRSSResponse(result)
            }
            atom {
                renderATOMResponse( result,params.max )
            }
            xml {
                render result as XML
            }
            json {
                render result as JSON
            }
        }
    }

    def testSearchClosure(c) {
        def builder = new GXContentBuilder()
        def b = builder.buildAsString(c)
        log.debug(b.toString())
    }

    def buildQuery(params) {
        log.debug("BuildQuery...");

        StringWriter sw = new StringWriter()

        if ( ( params != null ) && ( params.q != null ) )
        if(params.q.equals("*")){
            sw.write(params.q)
        }
        else{
            sw.write("(${params.q})")
        }
        else
        sw.write("*:*")

        //ensure search is always on public
        sw.write(" AND recstatus:\"public\"")
      

        // For each reverse mapping
        reversemap.each { mapping ->

            // log.debug("testing ${mapping.key}");

            // If the query string supplies a value for that mapped parameter
            if ( params[mapping.key] != null ) {

                // If we have a list of values, rather than a scalar
                if ( params[mapping.key].class == java.util.ArrayList) {
                    params[mapping.key].each { p ->  
                        sw.write(" AND ")
                        sw.write(mapping.value)
                        sw.write(":")
                
                        if(non_analyzed_fields.contains(mapping.value))
                        {
                            sw.write("${p}")
                        }
                        else
                        {
                            sw.write("\"${p}\"")
                        }
                    }
                }
                else {
                    // We are dealing with a single value, this is "a good thing" (TM)
                    // Only add the param if it's length is > 0 or we end up with really ugly URLs
                    // II : Changed to only do this if the value is NOT an *
                    if ( params[mapping.key].length() > 0 && ! ( params[mapping.key].equalsIgnoreCase('*') ) ) {
                        sw.write(" AND ")
                        // Write out the mapped field name, not the name from the source
                        sw.write(mapping.value)
                        sw.write(":")
            
                        // Couldn't be more wrong as it was: non_analyzed_fields.contains(params[mapping.key]) Should be checking mapped property, not source
                        if(non_analyzed_fields.contains(mapping.value))
                        {
                            sw.write("${params[mapping.key]}")
                        }
                        else
                        {
                            sw.write("\"${params[mapping.key]}\"")
                        }
                    }
                }
            }
        }

        def result = sw.toString();
        result;
    }

    def renderRSSResponse(results) {

        def output_elements = buildOutputElements(results.hits)

        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.rss(version: '2.0') {
            channel {
                title("XCRI Aggregator")
                description("XCRI Aggregator")
        "opensearch:totalResults"(results.hits.totalHits)
                // "opensearch:startIndex"(results.search_results.results.start)
        "opensearch:itemsPerPage"(10)
                output_elements.each { i ->  // For each record
                    entry {
                        i.each { tuple ->   // For each tuple in the record
              "${tuple[0]}"("${tuple[1]}")
                        }
                    }
                }
            }
        }

        render(contentType:"application/rss+xml", text: writer.toString())
    }


    def renderATOMResponse(results,hpp) {

        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        def output_elements = buildOutputElements(results.hits)

        xml.feed(xmlns:'http://www.w3.org/2005/Atom') {
            // add the top level information about this feed.
            title("XCRI Aggregator")
            description("XCRI Aggregator")
        "opensearch:totalResults"(results.hits.totalHits)
            // "opensearch:startIndex"(results.search_results.results.start)
        "opensearch:itemsPerPage"("${hpp}")
            // subtitle("Serving up my content")
            //id("uri:uuid:xxx-xxx-xxx-xxx")
            link(href:"http://coursedata.k-int.com")
            author {
                name("XCRI-DCAP")
            }
            //updated sdf.format(new Date());

            // for each entry we need to create an entry element
            output_elements.each { i ->
                entry {
                    i.each { tuple ->
                "${tuple[0]}"("${tuple[1]}")
                    }
                }
            }
        }

        render(contentType:'application/xtom+xml', text: writer.toString())
    }

    def buildOutputElements(searchresults) {
        // Result is an array of result elements
        def result = []

        searchresults.hits?.each { doc ->
            ////  log.debug("adding ${doc} ${doc.source.title}");
            def docinfo = [];

            docinfo.add(['dc.title',doc.source.title])
            docinfo.add(['dc.description',doc.source.description])
            docinfo.add(['dc.identifier',doc.source.title])
            // addField("dc.title", "dc.title", doc, docinfo)
            // addField("dc.description", "dc.description", doc, docinfo)
            // addField("dc.description", "dc.description", doc, docinfo)
            // addField("dc.identifier", "guid", doc, docinfo)
            // addField("modified", "pubdate", doc, docinfo)
            // docinfo.add(["link","${ApplicationHolder.application.config.ofs.pub.baseurl}/ofs/directory/${doc['authority_shortcode']}/${doc['aggregator.internal.id']}"])
            // if ( ( doc['lat'] != null ) && ( doc['lng'] != null ) ) {
            //   docinfo.add(["georss:point","${doc['lat']} ${doc['lng']}"])
            // }
            result.add(docinfo)
        }
        // println "Result ${result}"
        result
    }

    def count() {
      
        def result = [:]
        // Get hold of some services we might use ;)
        org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
        org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

    
        if ( params.q && params.q.length() > 0)
        {
            def query_str = buildQuery(params)
            log.debug("count query: ${query_str}");
                       
            def search = esclient.count{
                indices "courses"
                types "course"
                query {
                    query_string (query: query_str)
                }
            }
        
            result.hits = search.response.count
        }
    
        render result as JSON
    }

    def resolveTermIdentifier(term) {
        def db = mongoService.getMongo().getDB("xcri")
        // log.debug("Lookup ${term}");
        // log.debug(db.providers.findOne(identifier:term) as JSON)
    
        db.providers.findOne(identifier:term);
        // log.debug("looked up ${prov}");

    }
  
    def autocomplete() {

        def results = [:]
      
        org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
        org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()

          
        log.debug("autcomplete called for val " + params.term)

        if ( params.term ) {
      
            params.q = params.term
            // params.q = '*' + params.q + '*'
      
            if ( params.q && params.q.length() > 0) {
            
                // params.q = '*' + params.q + '*'
                def query_str = buildQuery(params)
            
                def search = esclient.search{
                    indices "courses"
                    types "course"
                    source {
                        from = "0"
                        size = "20"
                        query {
                            query_string (query: query_str, analyze_wildcard: true)
                        }
                        fields = ["title"]
                    }
                }
            
                results.hits = search.response.hits;
            }

            if ( results.hits.maxScore ) {
                log.debug("results.hits.maxscore: ${results.hits.maxScore}");
                if ( results.hits.maxScore == Float.NaN ) {
                    results.hits.maxScore = -1;
                }
            }
      
            try {
                render results as JSON
            }
            catch ( Exception e ) {
                log.error("Problem...",e);
            }
        }
    }
  
    def list_providers()
    {
        def provider = ['All':'']
      
        def db = mongoService.getMongo().getDB("xcri")
      
        db.providers.find().each {
            provider.(it.label) = it.identifier
        }
      
        return provider
    }
    def updateEsActivities(String query){
        
        org.elasticsearch.groovy.node.GNode esnode = ESWrapperService.getNode()
        org.elasticsearch.groovy.client.GClient esclient = esnode.getClient()
        def term_as_pojo = [:]

        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                
        term_as_pojo.date = formatter.format(date);
        term_as_pojo.term = query
        term_as_pojo.action = "Search"
                
        def client = esclient.index {
            index "activities"
            type "action"
            source term_as_pojo
        }
    }
}
