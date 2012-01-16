<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>${course.source.title} via ${course.source.provtitle} - XCRI Aggregator Portal</title>
  </head>
  <body>
    <div>
      <g:if test="${course.source.imageuri?.length() > 0}">
        <img src="${course.source.imageuri}" style="float:right" />
      </g:if>

      <h3><g:link controller="course" action="index" id="${course.source._id}">${course.source.title}</g:link> via ${course.source.provtitle} (${course.source.provid})</h3>

      <g:if test="${course.source.description?.length() > 0}">${course.source.description}<br/></g:if>

      Subjects: <g:join in="${course.source.subject}" delimiter=", "/><br/>
      Course Identifier: ${course.source.identifier}<br/>
      Course Link: ${course.source.url}<br/>
      Qualification: ${course.source.qual?.title} ( ${course.source.qual?.level} / ${course.source.qual?.awardedBy} )<br/>

      <g:if test="${params.debug=='true'}">
        <pre>For debugging, json follows ${course?.source}</pre>
      </g:if>
    </div>
  </body>
</html>
