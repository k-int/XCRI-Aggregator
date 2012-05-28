using System.Net;

string param = "hello";

string url = String.Format("http://somedomain.com/samplerequest?greeting={0}",param);

WebClient serviceRequest = new WebClient();
string response = serviceRequest.DownloadString(new Uri(url));

