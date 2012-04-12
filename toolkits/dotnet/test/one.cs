
using System;  
using System.IO;  
using System.Net;  
using System.Text;  
class one {
  static void Main() {
    System.Console.WriteLine("start");
  
    // Create the web request  
    HttpWebRequest request = WebRequest.Create("http://coursedata.k-int.com:9200/courses/course/_search?q=Painting") as HttpWebRequest;  
  
    // Get response  
    using (HttpWebResponse response = request.GetResponse() as HttpWebResponse)  
    {  
      // Get the response stream  
      StreamReader reader = new StreamReader(response.GetResponseStream());  
  
      // Console application output  
      Console.WriteLine(reader.ReadToEnd());  
    }  

    System.Console.WriteLine("Call complete");
  }
}
