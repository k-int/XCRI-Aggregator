# Test scripts for mongo search
mongo <<!!!
use xcri;
db.courses.find({ "lastModified" : { $gt : 0} }).limit(51);
!!!
