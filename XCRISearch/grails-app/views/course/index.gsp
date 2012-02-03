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

      <h1>${course.source.title} via ${course.source.provtitle} (${course.source.provid})</h1>

      <g:if test="${course.source.description?.length() > 0}">${course.source.description}<br/></g:if>

      <h4>Subjects</h4>
      <p><g:join in="${course.source.subject}" delimiter=", "/><br/></p>
      <h4>Course Identifier</h4><p>${course.source.identifier}</p>
      <h4>Course Link</h4><p>${course.source.url}</p>
      <h4>Qualification</h4><p>${course.source.qual?.title} ( ${course.source.qual?.level} / ${course.source.qual?.awardedBy} )</p>
      
      <g:if test="${course.source.abstract}">
        <h4>Abstract</h4><p>${course.source.abstract}</p>
      </g:if>
      <g:if test="${course.source.aim}">
        <h4>Course Aim</h4><p>${course.source.aim}</p>
      </g:if>
      <g:if test="${course.source.leadsTo}">
        <h4>Leads To</h4><p>${course.source.leadsTo}</p>
      </g:if>
      <g:if test="${course.source.careerOutcome}">
        <h4>Career Outcome</h4><p>${course.source.careerOutcome}</p>
      </g:if>     
      <g:if test="${course.source.syllabus}">
        <h4>Syllabus</h4><p>${course.source.syllabus}</p>
      </g:if>
      <g:if test="${course.source.structure}">
        <h4>Structure</h4><p>${course.source.structure}</p>
      </g:if>
      <g:if test="${course.source.assessmentStrategy}">
        <h4>Assessment Strategy</h4><p>${course.source.assessmentStrategy}</p>
      </g:if>
      <g:if test="${course.source.teachingStrategy}">
        <h4>Teaching Strategy</h4><p>${course.source.teachingStrategy}</p>
      </g:if>
      <g:if test="${course.source.careerOutcome}">
        <h4>Career Outcome</h4><p>${course.source.careerOutcome}</p>
      </g:if>
      <g:if test="${course.source.policy}">
        <h4>Policy</h4><p>${course.source.policy}</p>
      </g:if>
      <g:if test="${course.source.requiredResource}">
        <h4>Required Resources</h4><p>${course.source.requiredResource}</p>
      </g:if>
       <g:if test="${course.source.providedResource}">
        <h4>Provided Resources</h4><p>${course.source.providedResource}</p>
      </g:if>
      <g:if test="${course.source.support}">
        <h4>Support</h4><p>${course.source.support}</p>
      </g:if>
      <g:if test="${course.source.regulations}">
        <h4>Regulations</h4><p>${course.source.regulations}</p>
      </g:if>
      <g:if test="${course.source.specialFeature}">
        <h4>Special Features</h4><p>${course.source.specialFeature}</p>
      </g:if>
      <g:if test="${params.debug=='true'}">
        <pre>For debugging, json follows ${course?.source}</pre>
      </g:if>
    </div>
  </body>
</html>