using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Mvc.Ajax;
using System.Net;
using System.Globalization;
using System.IO;
using System.Web.Script.Serialization;
// using System.Runtime.CompilerServices.DynamicAttribute;
	

// http://weblogs.asp.net/hajan/archive/2010/07/23/javascriptserializer-dictionary-to-json-serialization-and-deserialization.aspx
// http://www.nikhilk.net/CSharp-Dynamic-Programming-JSON.aspx

namespace Controllers
{
	[HandleError]
	public class HomeController : Controller
	{
		public ActionResult Index ()
		{
			System.Diagnostics.Debug.WriteLine("HomeController::Index");
			
			Uri serviceUri = new Uri("http://coursedata.k-int.com:9200/courses/course/_search?q=painting", UriKind.Absolute);
			WebRequest wr = System.Net.WebRequest.Create(serviceUri);
			WebResponse es_response = (HttpWebResponse)wr.GetResponse();
            string jsonResponse = string.Empty;
            using (StreamReader sr = new StreamReader(es_response.GetResponseStream())) {
                jsonResponse = sr.ReadToEnd();
				
				// JsonReader jsonReader = new JsonReader(jsonText);
                // dynamic jsonObject = jsonReader.ReadValue();
				
				JavaScriptSerializer jss = new JavaScriptSerializer();
                dynamic data = jss.Deserialize<dynamic>(jsonResponse);
   			    ViewData ["esresponse"] = data;
            }
			
			ViewData ["Message"] = "XCRI Demo";
			
			return View ();
		}
	}
}

