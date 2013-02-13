#!/bin/bash

curl -X POST "http://coursedata.k-int.com:9200/courses/_search?pretty=true" -d '
  {
    "from" : 0,
    "size" : 0,
    "query" : { "query_string" : {"query" : "*"} },
    "facets" : {
      "tags" : { "terms" : {"field" : "qual.level", "all_terms":true} }
    }
  }
'
