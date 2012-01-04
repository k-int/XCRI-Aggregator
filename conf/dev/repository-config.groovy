aggr.system.name='XCRI-CAP Aggregator'

repo {
  settings {
    url='http://localhost:8080/HandlerRegistry'
    user='*********'
    pass='*********'
  }
}

com {
  k_int {
    aggregator {
      handlers {
        remoteRepo='http://localhost:8080'
      }
      aggregationServices {
        solr {
          default_core_name='DefaultSolr'
          DefaultSolr {
            name='Default SOLR Server'
            baseUrl='http://localhost:8085/solr'
            adminConfig {
              basedir='/usr/local/solr'
            }
          }
        }
      }
    }
  }
}

