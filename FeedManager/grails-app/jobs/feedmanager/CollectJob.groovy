package feedmanager

class CollectJob {

  // We only want 1 of these running at a time...
  // Trying with this commented out, as grails run-app deadlocks with the timer task running.. 
  //def concurrent = false
  def feedRunnerService

  static triggers = {
    cron name: 'CollectJobTrigger', cronExpression: "0 0/2 * * * ?", startDelay:60000
  }

  static Boolean running = false;
  static int processed_counter = 0;
  static int pending_counter = 0;
  static long current_feed_id = 0;
  static long feed_start_time=0;

  def group = "CollectJobGroup"

  def execute(){
    log.debug("Collect Job Running")
    boolean already_running = false;
    synchronized(this) {
      if ( running ) {
        already_running = true;
      }
      else {
        running = true;
      }
    }


    if ( !already_running ) {
      pending_counter=0;
      processed_counter=0;
      try {
        def feeds = com.k_int.feedmanager.SingleFileDatafeed.findAll()
        pending_counter=feeds.size()
        feeds.each { feed ->
          current_feed_id = feed.id;
          feed_start_time = System.currentTimeMillis();
          def ms_since_last_check = feed_start_time - feed.lastCheck;
          log.debug("Checking ${feed.baseurl}, force=${feed.forceHarvest}, lastCheck=${feed.lastCheck}, interval=${feed.checkInterval}, MS since last check: ${ms_since_last_check}");
          log.debug("About to try feed ${processed_counter++} out of ${pending_counter}");
          if ( ( feed.active ) &&
               ( ( feed.forceHarvest ) ||
                 ( feed.lastCheck == null ) ||
                 ( feed.lastCheck == 0 ) || 
                 ( ms_since_last_check > feed.checkInterval ) ) ) {
            log.debug("Collecting......");
            feedRunnerService.collect(feed.forceHarvest, feed.id)
          }
          else {
            log.debug("Not collecting");
          }
        }
      }
      catch (Exception e) {
        log.error("Problem in collect job",e);
      }
      finally {
        log.debug("Collect job completed");
        running = false;
      }
    }
    else {
      log.debug("Feed collector job already running, not starting a second thread. Current thread on feed ${processed_counter} out of ${pending_counter}.");
      log.debug("  -> feedid ${current_feed_id} - Started processing at ${feed_start_time}, giving ${System.currentTimeMillis()-feed_start_time} elasped");
    }
  }
}
