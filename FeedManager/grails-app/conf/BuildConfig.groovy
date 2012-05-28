grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
	//mavenRepo "https://oss.sonatype.org/content/repositories/releases"
        mavenRepo "https://oss.sonatype.org/content/repositories/releases"
    }
    dependencies {
       // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
       runtime 'org.elasticsearch:elasticsearch:0.19.4'
       runtime 'org.elasticsearch:elasticsearch-lang-groovy:1.1.0'
       runtime 'mysql:mysql-connector-java:5.1.18'
       runtime 'commons-fileupload:commons-fileupload:1.2.2'
       runtime 'org.apache.httpcomponents:httpmime:4.1.2'
    }

    plugins {
        compile ":hibernate:$grailsVersion"
        compile ":jquery:1.7"
        compile ":resources:1.1.3"
        build ":tomcat:$grailsVersion"
    }
}
