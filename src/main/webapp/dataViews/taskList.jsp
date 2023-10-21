<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
<title>TaskList</title>
<style>
    table { empty-cells: show; }
</style>
</head>
<body>

<jsp:useBean id="planDates" class="java.util.HashMap" scope="request"/>
<c:forEach var="row" items="${pdash.query['select tdf.planItem.id, tdf.taskDate from TaskDateFact as tdf where tdf.measurementType.name=? ']['Plan']}">
<c:set target="${planDates}" property="${row[0]}" value="${row[1]}"/>
</c:forEach>

<jsp:useBean id="replanDates" class="java.util.HashMap" scope="request"/>
<c:forEach var="row" items="${pdash.query['select tdf.planItem.id, tdf.taskDate from TaskDateFact as tdf where tdf.measurementType.name=? ']['Replan']}">
<c:set target="${replanDates}" property="${row[0]}" value="${row[1]}"/>
</c:forEach>

<jsp:useBean id="forecastDates" class="java.util.HashMap" scope="request"/>
<c:forEach var="row" items="${pdash.query['select tdf.planItem.id, tdf.taskDate from TaskDateFact as tdf where tdf.measurementType.name=? ']['Forecast']}">
<c:set target="${forecastDates}" property="${row[0]}" value="${row[1]}"/>
</c:forEach>    

<h1><c:out value="${pdash.projectPath}"/></h1>

<h2>Task List</h2>
<table border class="display">
<thead>
<tr>
<th>Id</th>
<th>Project/Task</th>
<th>Project</th>
<th>WbsElement</th>
<th>Process</th>
<th>PhaseOrdinal</th>
<th>Phase</th>
<th>PlanTime</th>
<th>ActualTime</th>
<th>PercentSpent</th>
<th>PlanDate</th>
<th>ReplanDate</th>
<th>ForecastDate</th>
<th>ActualDate</th>
<th>IsComplete</th>
</tr>
</thead>
<tbody >

<c:forEach var="task" items="${pdash.query['from TaskStatusFact as t']}">

<tr >
    <c:set var="planItemId">${task.planItem.key}</c:set> 
    <c:set var="planItem">${task.planItem}</c:set> 

    <td><c:out value="${planItemId}" /></td>
    <td><c:out value="${planItem}" /></td>
    <td><c:out value="${task.planItem.project}" /></td>
    <td><c:out value="${task.planItem.wbsElement}" /></td>
    <td><c:out value="${task.planItem.phase.process}" /></td>
    <td><c:out value="${task.planItem.phase.ordinal}" /></td>
    <td><c:out value="${task.planItem.phase.shortName}" /></td>
    <td><fmt:formatNumber value="${task.planTimeMin}"/></td>
    <td><fmt:formatNumber value="${task.actualTimeMin}"/></td>
    <td><c:if test="${task.planTimeMin > 0}"><fmt:formatNumber type="percent"
        value="${task.actualTimeMin / task.planTimeMin}"/></c:if></td>
    <td><c:out value="${planDates[planItemId]}"/></td>     
    <td><c:out value="${replanDates[planItemId]}"/></td>
    <td><c:out value="${forecastDates[planItemId]}"/></td>
    <td><c:out value="${task.actualCompletionDate}" /></td>  
    <td>
        <c:choose>
            <c:when test="${task.actualCompletionDate != null}"><c:out value="1"/></c:when>
            <c:otherwise><c:out value="0"/></c:otherwise>
        </c:choose>
    </td>

</tr>

</c:forEach>
</tbody>
</table>

</body>
</html>
