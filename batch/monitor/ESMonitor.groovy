#!/usr/bin/groovy

@GrabResolver(name='es', root='https://oss.sonatype.org/content/repositories/releases')
// @Grab(group='com.gmongo', module='gmongo', version='0.9.2')
@Grapes([
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0')
])

def starttime = System.currentTimeMillis();
println("Startup.. Initialising at ${starttime}");

def monitor = new RepoMonitor();
def tse = new TripleStoreService();

println("initialise triple store");
tse.init();

println("Connect to mongo");
def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("xcri")

println("Monitor starting after ${System.currentTimeMillis() - starttime}");

monitor.iterateLatest(db,'providers', -1) { jsonobj ->
  
  def writer = new StringWriter()
  def xml = new groovy.xml.MarkupBuilder(writer)
  xml.setOmitEmptyAttributes(true);
  xml.setOmitNullAttributes(true);
  xml.'xcri:provider'('rdf:about':"urn:xcri:provider:${jsonobj._id}",
                      'xmlns:rdf':'http://www.w3.org/1999/02/22-rdf-syntax-ns#',
                      'xmlns:dcterms':'http://purl.org/dc/terms/',
                      'xmlns:dc':'http://purl.org/dc/elements/1.1/',
                      'xmlns:xcri':'http://xcri.org/profiles/catalog/1.2/') {
    'dc:title'(jsonobj.label)
  }

  def result = writer.toString();
  tse.removeGraph("urn:xcri:course:${jsonobj._id}");
  tse.update(result, "urn:xcri:course:${jsonobj._id}", 'application/rdf');
  println("Updated provider ( ${System.currentTimeMillis() - starttime} )");
}

monitor.iterateLatest(db,'courses', -1) { jsonobj ->
  println("buildRDFXML");
  def writer = new StringWriter()
  def xml = new groovy.xml.MarkupBuilder(writer)
  xml.setOmitEmptyAttributes(true);
  xml.setOmitNullAttributes(true);

  // println ("Processing record ${jsonobj}");

  //
  // see http://svn.cetis.ac.uk/xcri/trunk/bindings/rdf/xcri_rdfs.xml
  //
  // xml.'rdf:RDF'('xmlns:rdf':'http://www.w3.org/1999/02/22-rdf-syntax-ns#',
  //            'xmlns:dcterms':'http://purl.org/dc/terms/',
  //            'xmlns:dc':'http://purl.org/dc/elements/1.1/',
  //            'xmlns:xcri':'http://xcri.org/profiles/catalog/1.2/') {
  //            'rdf:resource'('rdf:about':"urn:xcri:course:${jsonobj._id}") {
  xml.'xcri:course'('rdf:about':"urn:xcri:course:${jsonobj._id}",
                    'xmlns:rdf':'http://www.w3.org/1999/02/22-rdf-syntax-ns#',
                    'xmlns:dcterms':'http://purl.org/dc/terms/',
                    'xmlns:dc':'http://purl.org/dc/elements/1.1/',
                    'xmlns:xcri':'http://xcri.org/profiles/catalog/1.2/') {
    'dc:title'(jsonobj.title)
    'xcri:provid'("urn:xcri:provider:${jsonobj.provid}")
    'xcri:provname'(jsonobj.provname)
    'xcri:uri'(jsonobj.url)
    'xcri:abstract'(jsonobj.description)
  }

  def result = writer.toString();
  tse.removeGraph("urn:xcri:course:${jsonobj._id}");
  tse.update(result, "urn:xcri:course:${jsonobj._id}", 'application/rdf');
  println("Updated course ( ${System.currentTimeMillis() - starttime} )");
}

println("Completed after ${System.currentTimeMillis() - starttime}");
