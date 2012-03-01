#!/usr/bin/groovy

@GrabResolver(name='es', root='https://oss.sonatype.org/content/repositories/releases')
// @Grab(group='com.gmongo', module='gmongo', version='0.9.2')
@Grapes([
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0')
])

def starttime = System.currentTimeMillis();

// def l = new Loader();
// l.load('uk_gaz_with_geo-2011-05-08.csv');
println("ES Monitor run completed");

def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("xcri")

iterateLatest(db,'courses') { jsonobj ->
  println("buildRDFXML");
  def writer = new StringWriter()
  def xml = new groovy.xml.MarkupBuilder(writer)
  xml.setOmitEmptyAttributes(true);
  xml.setOmitNullAttributes(true);

  // println ("Processing record ${jsonobj}");

  //
  // see http://svn.cetis.ac.uk/xcri/trunk/bindings/rdf/xcri_rdfs.xml
  //
  xml.'rdf:RDF'('xmlns':'http://www.w3.org/1999/02/22-rdf-syntax-ns#',
                'xmlns:dcterms':'http://purl.org/dc/terms/',
                'xmlns:dc':'http://purl.org/dc/elements/1.1/',
                'xmlns:xcri':'http://xcri.org/profiles/catalog/1.2/') {
    'rdf:resource'('rdf:about':"urn:xcri:course:${jsonobj._id}") {
      'dc:title'(jsonobj.title)
      'xcri:provid'(jsonobj.provid)
      'xcri:provname'(jsonobj.provname)
      'xcri:uri'(jsonobj.url)
      'xcri:abstract'(jsonobj.description)
    }
  }

  def result = writer.toString();

  // println(result);
}

def iterateLatest(db, collection, processing_closure) {

  // Lookup a monitor record for the identified collection
  // Create one if it doesn't exist.
  def monitor_info = db.monitors.findOne(coll:collection);
  if ( monitor_info == null ) {
    println("Create new monitor info for ${collection}");
    monitor_info = [:]
    monitor_info.coll = collection
    monitor_info.maxts = 0;
    monitor_info.maxid = ''
  }
  else {
    println("Using existing monitor info");
  }

  println("Finding all entries from ${collection} where lastModified > ${monitor_info.maxts} and id > \"${monitor_info.maxid}\"");

  def next=true;
  while(next) {
    def batch = db."${collection}".find( [ lastModified : [ $gt : 0 ] ] ).limit(51);

    def i=0;
    batch.each { r ->
      xml = processing_closure.call(r)
      // println("* ${i++}");
    }

    // println("batch: ${batch}");

    println("Saving monitor info");
    db.monitors.save(monitor_info);

    next=false;
  }

}
