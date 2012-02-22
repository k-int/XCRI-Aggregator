#!/bin/bash


# export tc=/home/xcri/apache-tomcat-7.0.23/bin
export tc=~/apache-tomcat

cd $tc/bin
# ./shutdown sh
rm -Rf ../webapps/FeedManager ../webapps/HandlerRegistry ../webapps/repository ../webapps/discover
rm -Rf ../logs/*
cd /home/xcri
rm -Rf src
mkdir src
cd src
git clone git@github.com:k-int/XCRI-Aggregator.git
git clone git@github.com:k-int/AggregatorCore.git


cd ~/src/aggregator
git checkout release
git pull
cd ~/src/aggregator/HandlerRegistry
grails prod war
cd ~/src/aggregator/repository
grails prod war


cd ~/src/XCRI-Aggregator
git checkout release
git pull
cd ~/src/XCRI-Aggregator/XCRISearch
grails prod war
cd ~/src/XCRI-Aggregator/FeedManager
grails prod war


cp ~/src/XCRI-Aggregator/FeedManager/target/FeedManager-0.1.war $tc/webapps/FeedManager.war
cp ~/src/aggregator/repository/target/repository-0.1.war $tc/webapps/repository.war
cp ~/src/aggregator/HandlerRegistry/target/HandlerRegistry-0.1.war $tc/webapps/HandlerRegistry.war
cp ~/src/XCRI-Aggregator/XCRISearch/target/XCRISearch-0.1.war $tc/webapps/discover.war

