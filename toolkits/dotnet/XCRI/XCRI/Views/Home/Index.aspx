<%@ Page Language="C#" Inherits="System.Web.Mvc.ViewPage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
	<title></title>
</head>
<body>
	<div>
		<%= Html.Encode(ViewData["Message"]) %>

	    <% using (Html.BeginForm()){ %>
<p>
<label for="FirstName">Course Name:</label>
<%= Html.TextBox("Course Name") %>
</p>
<p>
<input type="submit" value="Register" />
</p>
<%}%>

		
		<% if( ViewData["esresponse"] != null ) { 
		  dynamic esresp = (dynamic) ViewData["esresponse"];
		  %>
		             Hits: <%= esresp.hits.total %> <br/>
		  <% 
		  
		    foreach ( dynamic hit in esresp.hits.hits ) {
		      %>
		        Course Title: <%= hit._source.title %> <br/>
		        Course Provider: <%= hit._source.provtitle %> <br/>
		        <hr/>
		      <%
		    }
		  }  
		%>
		
	</div>
</body>

