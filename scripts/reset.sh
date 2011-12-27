#!/bin/bash


# Clear down mongo

mongo <<!!!
use xcri
db.dropDatabase()
!!!


# Clear down ES indexes
curl -XDELETE 'http://localhost:9200/courses/course/_query?q=*:*'
