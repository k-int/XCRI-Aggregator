<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
	<title></title>
</head>
<body>
	<div>
		<%= Html.Encode(ViewData["Message"]) %>
			
		<% if( ViewData["esresponse"] != null ) { 
		  Dictionary<String,object> hitsinfo = (Dictionary<String,object>) ViewData["esresponse"];
		  Dictionary<String,object> h2 = (Dictionary<String,object>) hitsinfo["hits"];
		  %>
			 Response: <%= ViewData["esresponse"] %> <br/>
		
			 Hitcount: <%= h2["total"] %> <br/>
		<% 
		    foreach(KeyValuePair<String,object> entry in hitsinfo) {
		      %> <%=entry.Key%> <br/> <%
            }
		  }  
		%>
		
			<% using (Html.BeginForm()){ %>
<p>
<label for="FirstName">Course Name:</label>
<%= Html.TextBox("Course Name") %>
</p>
<p>
<input type="submit" value="Register" />
</p>
<%}%>
	</div>
</body>

