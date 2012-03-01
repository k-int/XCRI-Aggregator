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

def next=true;
while(next) {
  def mongo = new com.gmongo.GMongo()
  def db = mongo.getDB("xcri")

  def batch = db.courses.find( [ lastModified : [ $gt : 0 ] ] ).limit(51);

  def i=0;
  batch.each { r ->
    println("* ${i++}");
  }

  println("batch: ${batch}");

  next=false;
}
