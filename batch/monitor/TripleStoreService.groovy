import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import grails.converters.*
import java.security.MessageDigest


class TripleStoreService {

  def fourstore_endpoint = null;
  
  @javax.annotation.PostConstruct
  def init() {
    println("init...");
    if ( fourstore_endpoint == null ) {
      fourstore_endpoint = new HTTPBuilder( 'http://localhost:9000/' )
    }
    else {  
      println("init already called");
    }
    // fourstore_endpoint.auth.basic feed_definition.target.identity, feed_definition.target.credentials
    // this.updateURL = new URL(baseurl + "/data/");
  }

  def update(graph, graph_uri, mimetype) { // throws MalformedURLException, ProtocolException, IOException {

    if ( fourstore_endpoint == null )
      throw new RuntimeException("not initialised");

    try {
      println("update ${graph_uri} - upload graph ${graph}");

      fourstore_endpoint.request(POST) { request ->
        // requestContentType = 'multipart/form-data'
        requestContentType = 'application/x-www-form-urlencoded'

        uri.path = 'data/'

        body =  [ 
          'mime-type' : 'mimetype' , 
          'graph' : graph_uri,
          'data' : graph
          // 'graph' : URLEncoder.encode(graph_uri, "UTF-8"),
          // 'data' : URLEncoder.encode(graph, "UTF-8")
        ] 

        response.success = { resp, data ->
          println("Update graph \"${graph_uri}\"response status: ${resp.statusLine} ${data}")
        }

        response.failure = { resp ->
          println("Failure - ${resp.statusLine} ${resp}");
        }
      }
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
    finally {
      println("Complete");
    }
  }

  def removeGraph(graph_uri) {
    // URL delete_url = new URL(baseurl + "/data/"+uri);
    // HttpURLConnection connection = (HttpURLConnection) this.updateURL.openConnection();
    // connection.setDoOutput(true);
    // connection.setRequestMethod("DELETE");
    // connection.connect();
    println("Delete ${graph_uri}");
    fourstore_endpoint.request(DELETE) { request ->
      uri.path = "data/${graph_uri}"

      response.success = { resp, data ->
        println("delete graph \"${graph_uri}\" : response status: ${resp.statusLine} ${data}")
      }

      response.failure = { resp ->
        // println("Failure - ${resp.statusLine} ${resp}");
      }
    }
  }
}
