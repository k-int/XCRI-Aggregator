<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta http-equiv="Cache-Control" content="no-cache"/>
  <title>XCRI-CAP Aggregator</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
  <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
  <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'fonts.css')}" type="text/css">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'layout.css')}" type="text/css">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'nav.css')}" type="text/css">
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'forms.css')}" type="text/css">
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js" type="text/javascript"></script>
  <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/jquery-ui.min.js" type="text/javascript"></script>
  <g:layoutHead/>
  <r:layoutResources />
  <script>
  	/* HTML5 elements for ie */
	document.createElement('header');
	document.createElement('nav');
	document.createElement('article');
	document.createElement('footer');
  </script>
</head>
<body>
	<header>
		<div class="inner-cont">
			<h1>XCRI-CAP Aggregator</h1>
		</div>
	</header>
	<nav>
		<div class="inner-cont">
			<ul>
			    <li><g:link class="home"><span>Home</span></g:link></li>
			    <li><g:link controller="home" action="index" id="${params.id}" class="feeds"><span>My Feeds</span></g:link></li>
  			</ul>
			<ul>
			    <li><g:link controller="feed" action="dashboard" id="${params.id}" class="dashboard"><span>Dashboard</span></g:link></li>
			    <li><g:link controller="feed" action="search" id="${params.id}" class="search"><span>Search</span></g:link></li>
			    <li><g:link controller="feed" action="console" id="${params.id}" class="console"><span>Console</span></g:link></li>
  			</ul>
  			<ul style="float:right">
			    <li><g:link controller="home" action="index" id="${params.id}" class="logout"><span>Logout</span></g:link></li>
  			</ul>
		</div>
	</nav>
	<div id="content">
		<div class="inner-cont">
		 	<g:layoutBody/>
		</div>
	</div>
	<footer>
		<div class="inner-cont" role="contentinfo">
			<span>XCRI-CAP Aggregator &copy; 2011 - <a href="http://www.k-int.com" target="_blank" class="grey-contrast-txt">Knowledge Integration</a></span>
		</div>
	</footer>
	<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>

	<g:javascript library="application"/>
	<r:layoutResources />
</body>
</html>
