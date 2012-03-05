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

      <h1>${course.source.title} via ${course.source.provtitle} (${course.source.provid})
        <span class="h-link-small">&lt;<g:link controller="course" action="index" id="${course.source._id}" target="_blank" params="[format:'json']">JSON</g:link>&gt;</span>
        <span class="h-link-small">&lt;<g:link controller="course" action="index" id="${course.source._id}" target="_blank" params="[format:'xml']">XML</g:link>&gt;</span>
      </h1>

      <g:if test="${course.source.description?.length() > 0}">${course.source.description}<br/></g:if>

      <g:if test="${course.source.subject}">
      <h4>Subjects</h4>
      <p>
      <g:join in="${course.source.subject}" delimiter=", "/><br/>
      <g:if test="${course.source.subjectKeywords && course.source.subjectKeywords.size() > 0}">
      <span style="color:gray">Keywords: <g:join in="${course.source.subjectKeywords}" delimiter=", "/></span>
      </g:if>
      </p>
      </g:if>
      <h4>Course Identifier</h4><p>${course.source.identifier}</p>
      <h4>Course Link</h4><a href="${course.source.url}">${course.source.url}</a></p>
      <h4>Qualifications</h4>
      <p>
      <g:if test="${course.source.qual.title}">${course.source.qual.title}</g:if><g:if test="${course.source.qual.level}"> (Level: ${course.source.qual.level})</g:if>
      <ul>   
            <g:if test="${course.source.qual.type}"><li>${course.source.qual.type}</li></g:if>
            <g:if test="${course.source.qual.description}"><li>${course.source.qual.description} </li></g:if>
            <g:if test="${course.source.qual.awardedBy}"><li>Awarded by ${course.source.qual.awardedBy}</li></g:if>
            <g:if test="${course.source.qual.accreditedBy}"><li>Accredited by ${course.source.qual.accreditedBy}</li></g:if>
      </ul>
      </p>

      <g:if test="${course.source.presentations && course.source.presentations.size() > 0}">    
      <h4>Variations</h4>
      <p>
          <g:each var="presentation" in="${course.source.presentations}">
          <span style="font-weight: bold">${presentation.identifier}</span>
          <br/>
          <g:if test="${presentation.description}">
          ${presentation.description}<br/>
          </g:if>
          <ul>   
                <li>Running from ${presentation.start} until ${presentation.end} <g:if test="(${presentation.duration}">(${presentation.duration})</g:if></li>
                <li>Apply for this course between ${presentation.applicationsOpen} and ${presentation.applicationsClose}</li>
                <g:if test="${presentation.applyTo}"> <li>Applications can be submitted to ${presentation.applyTo}</li></g:if>
                <g:if test="${presentation.enquireTo}"><li>Enquiries can be made to ${presentation.enquireTo}</li></g:if>
                <g:if test="${presentation.cost}"><li>The cost of this course is ${presentation.cost}</li></g:if>
                <g:if test="${presentation.venue}">
                <li>Taught at ${presentation.venue.name}, ${presentation.venue.street}, ${presentation.venue.town}, ${presentation.venue.postcode}</li>
                </g:if>
          </ul>
          <br/>
          </g:each>
      </p>
      </g:if>
      
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
