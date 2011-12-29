package feedmanager

class CollectJob {

  // We only want 1 of these running at a time...
  // Trying with this commented out, as grails run-app deadlocks with the timer task running.. 
  //def concurrent = false
  def feedRunnerService

  static triggers = {
    cron name: 'CollectJobTrigger', cronExpression: "0 0/10 * * * ?"
  }

  def group = "CollectJobGroup"

  def execute(){
    log.debug("Collect Job Running")
    def feeds = com.k_int.feedmanager.SingleFileDatafeed.findAll()
    feeds.each { feed ->
      log.debug("Checking ${feed.baseurl}");
      if ( ( feed.active ) &&
           ( ( feed.lastCheck == 0 ) || ( System.currentTimeMillis() - feed.lastCheck > feed.checkInterval ) ) ) {
        log.debug("Collecting......");
        feedRunnerService.collect(feed)
      }
    }
    log.debug("Collect job completed");
  }
}
