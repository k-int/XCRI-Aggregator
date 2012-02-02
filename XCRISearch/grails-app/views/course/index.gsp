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

      <h3>${course.source.title} via ${course.source.provtitle} (${course.source.provid})</h3>

      <g:if test="${course.source.description?.length() > 0}">${course.source.description}<br/></g:if>

      <h3>Subjects</h3>
      <p><g:join in="${course.source.subject}" delimiter=", "/><br/></p>
      <h3>Course Identifier</h3><p>${course.source.identifier}</p>
      <h3>Course Link</h3><p>${course.source.url}</p>
      <h3>Qualification</h3><p>${course.source.qual?.title} ( ${course.source.qual?.level} / ${course.source.qual?.awardedBy} )</p>
      
      <g:if test="${course.source.abstract}">
        <h3>Abstract</h3><p>${course.source.abstract}</p>
      </g:if>
      <g:if test="${course.source.aim}">
        <h3>Course Aim</h3><p>${course.source.aim}</p>
      </g:if>
      <g:if test="${course.source.leadsTo}">
        <h3>Leads To</h3><p>${course.source.leadsTo}</p>
      </g:if>
      <g:if test="${course.source.careerOutcome}">
        <h3>Career Outcome</h3><p>${course.source.careerOutcome}</p>
      </g:if>     
      <g:if test="${course.source.syllabus}">
        <h3>Syllabus</h3><p>${course.source.syllabus}</p>
      </g:if>
      <g:if test="${course.source.assessmentStrategy}">
        <h3>Assessment Strategy</h3><p>${course.source.assessmentStrategy}</p>
      </g:if>
      <g:if test="${course.source.teachingStrategy}">
        <h3>Teaching Strategy</h3><p>${course.source.teachingStrategy}</p>
      </g:if>
       <g:if test="${course.source.careerOutcome}">
        <h3>Career Outcome</h3><p>${course.source.careerOutcome}</p>
      </g:if>
       <g:if test="${course.source.policy}">
        <h3>Policy</h3><p>${course.source.policy}</p>
      </g:if>
      <g:if test="${course.source.support}">
        <h3>Support</h3><p>${course.source.support}</p>
      </g:if>
      

      <g:if test="${params.debug=='true'}">
        <pre>For debugging, json follows ${course?.source}</pre>
      </g:if>
    </div>
  </body>
</html>


"metadataKeywords": ["xcri:metadataKeywords","xcriterms:metadataKeywords"],
                              "abstract": ["xcri:abstract","xcriTerms:abstract"],
                              "careerOutcome": ["xcri:careerOutcome","xcriTerms:careerOutcome","Career Outcome"],
                              "prerequisites": ["xcri:prerequisites","xcriTerms:prerequisites","Entry Profile"],
                              "indicativeResource": ["xcri:indicativeResource","xcriTerms:indicativeResource","Indicative Resource"],
                              "assessmentStrategy":["xcri:assessmentStrategy","xcriTerms:assessmentStrategy","Assessment Strategy"],
                              "aim":["xcri:aim","xcriTerms:aim","Aim","terms:topic"],
                              "learningOutcome":["xcri:learningOutcome","xcriTerms:learningOutcome","Learning Outcome"],
                              "syllabus": ["xcri:syllabus","xcriTerms:syllabus","Syllabus"],   
                              "support": ["xcri:support","xcriTerms:support","Support"],
                              "teachingStrategy": ["xcri:teachingStrategy","xcriTerms:teachingStrategy","Teaching Strategy"],
                              "structure": ["xcri:structure","xcriTerms:structure","Structure"],
                              "specialFeature": ["xcri:specialFeature","xcriTerms:specialFeature","Special Feature"],
                              "leadsTo": ["xcri:leadsTo","xcriTerms:leadsTo","Leads To"],
                              "requiredResource":["xcri:requiredResource","xcriTerms:requiredResource","Required Resource"],
                              "providedResource":["xcri:providedResource","xcriTerms:providedResource","Provided Resource"],
                              "policy":["xcri:policy","xcriTerms:policy","Policy"],
                              "regulations":["xcri:regulations","xcriTerms:regulations","Policy"]