<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>${course.source.title} via ${course.source.provtitle} - XCRI Aggregator Portal</title>
  </head>
  <body>
    This is a course details page
    <h1>${course.source.title} via ${course.source.provtitle}</h1>
    <p>
      ${course.source.description}
    </p>
    <ul class="result">
   		<li><g:img dir="images/icons" file="subject.png"/><span><g:join in="${course.source.subject}" delimiter=", "/></span></li>
    	<li><g:img dir="images/icons" file="identifier.png"/><span>${course.source.identifier}</span></li>
    	<li><g:img dir="images/icons" file="link.png"/><span>${course.source.url}</span></li>
    </ul>
    <pre>
      ${course.source}
    </pre>
  </body>
</html>
