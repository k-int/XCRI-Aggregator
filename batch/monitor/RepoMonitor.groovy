class RepoMonitor {

  def iterateLatest(db, collection, max_iterations, processing_closure) {

    println("iterateLatest");

    // Lookup a monitor record for the identified collection
    // Create one if it doesn't exist.
    def monitor_info = db.monitors.findOne(coll:collection);
    if ( monitor_info == null ) {
      println("Create new monitor info for ${collection}");
      monitor_info = [:]
      monitor_info.coll = collection
      monitor_info.maxts = 0;
      monitor_info.maxid = null;
    }
    else {
      println("Using existing monitor info");
    }

    def next=true;  
    def batch_size = 10;
    def iteration_count = 0;

    while( ( ( max_iterations == -1 ) || ( iteration_count < max_iterations ) ) && next) {
      next=false;
      println("${next} Finding all entries from ${collection} where lastModified > ${monitor_info.maxts} and id > \"${monitor_info.maxid}\"");
      def batch

      if ( monitor_info.maxid != null ) {
        batch = db."${collection}".find( [ lastModified : [ $gt : monitor_info.maxts ], _id : [ $gt : monitor_info.maxid ]  ] ).limit(batch_size+1).sort(lastModified:1,_id:1);
      } else {
        batch = db."${collection}".find( [ lastModified : [ $gt : monitor_info.maxts ] ] ).limit(batch_size+1).sort(lastModified:1,_id:1);
      }

      println("Query completed, batchsize = ${batch.size()}");

      int counter = 0;

      batch.each { r ->
        if ( counter < batch_size ) {
          counter++;
          processing_closure.call(r)
          monitor_info.maxts = r.lastModified;
          monitor_info.maxid = r._id;
          println("* ${iteration_count}/${counter}/${batch_size} : ${monitor_info.maxts}, ${monitor_info.maxid}");
        }
        else {
          // We've reached record batch_size+1, which means there is at least 1 more record to process. We should loop,
          // assuming we haven't passed max_iterations
          println("Counter has reached ${batch_size+1}, reset maxid");
          next=true
        }
      }
      println("Saving monitor info ${monitor_info}");
      iteration_count++;
      db.monitors.save(monitor_info);
    }

    println("Complere");
  }
}
