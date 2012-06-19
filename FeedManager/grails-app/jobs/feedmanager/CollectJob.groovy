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
      try {
        def feeds = com.k_int.feedmanager.SingleFileDatafeed.findAll()
        feeds.each { feed ->
          def ms_since_last_check = System.currentTimeMillis() - feed.lastCheck;
          log.debug("Checking ${feed.baseurl}, force=${feed.forceHarvest}, lastCheck=${feed.lastCheck}, interval=${feed.checkInterval}, MS since last check: ${ms_since_last_check}");
          if ( ( feed.active ) &&
               ( ( feed.forceHarvest ) ||
                 ( feed.lastCheck == null ) ||
                 ( feed.lastCheck == 0 ) || 
                 ( ms_since_last_check > feed.checkInterval ) ) ) {
            log.debug("Collecting......");
            feedRunnerService.collect(false, feed.id)
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
      log.debug("Feed collector job already running, not starting a second thread");
    }
  }
}
