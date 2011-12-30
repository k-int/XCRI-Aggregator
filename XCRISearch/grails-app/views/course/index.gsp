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
    <p>
      Subjects: <g:join in="${course.source.subject}" delimiter=", "/><br/>
      Identifier: ${course.source.identifier}<br/>
      URL: ${course.source.url}<br/>
    </p>
    <p>
      ${course.source}
    </p>
  </body>
</html>
