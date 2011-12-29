#!/bin/bash


# Clear down mongo

mongo <<!!!
use xcri
db.dropDatabase()
!!!


# Clear down ES indexes
curl -XDELETE 'http://localhost:9200/courses/course/_query?q=*:*'

echo clear db

mysql -u k-int -pk-int Aggr3Live <<!!!
use Aggr3Live;
drop table resource;
drop table event_handler;
drop table deposit_event;
use hreplive;
drop table handler;
drop table handler_revision;
use FeedManagerLive
drop table aggregation_service;
drop table datafeed;
drop table shiro_role;
drop table shiro_role_permissions;
drop table shiro_user;
drop table shiro_user_permissions;
drop table shiro_user_roles;
!!!


echo done

