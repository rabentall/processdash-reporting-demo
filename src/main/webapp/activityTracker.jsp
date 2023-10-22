<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<html>
<head>
<title>Activity Tracker</title>

<link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.css" />  
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.js"></script>
<script src="script/activityTracker.js" ></script>

</head>


<script>

//
// Flat tasklist:
//
const taskList_ = [
<c:forEach var="task" items="${pdash.query['from TaskStatusFact as t where t.actualCompletionDate = null']}" varStatus="status" >
    [
        '${task.planItem}',
        '${task.planTimeMin}',
        '${task.actualTimeMin}'
    ]<c:if test="${!status.last}">,</c:if> 
</c:forEach>];

//
// Document ready handler:
//
$(document).ready(() =>
{
  initTaskListTable(taskList_);
});

//
// Callback for document ready:
//

//initTable();
</script>

<body>

<h2>Activity Tracker</h2>
<table id="timerTable" class="display" style="width:100%">

</table>
</body>
</html>

<!--

<thead>
<tr>
<th>Timer</th>
<th>Project/Task</th>
<th>Plan Time</th>
<th>Actual Time</th>
<th>Percent Spent</th>
<th>Plan Date</th>
<th>Replan Date</th>
<th>Forecast Date</th>
</tr>
</thead>
<tbody >


<tr>
    <c:set var="planItem">${task.planItem}</c:set> 
    <c:set var="planItemId">${task.planItem.key}</c:set>
    <td><A HREF= "javascript:javascript:void(0)" onClick="javascript:toggleTimer('${planItem}')"><img border="0" title="Start timing" src="/control/startTiming.png"></A></td>
    <td><c:out value="${task.planItem}" /></td>
    <td><fmt:formatNumber value="${task.planTimeMin}"/></td>
    <td><fmt:formatNumber value="${task.actualTimeMin}"/></td>
    <td><c:if test="${task.planTimeMin > 0}"><fmt:formatNumber type="percent"
        value="${task.actualTimeMin / task.planTimeMin}"/></c:if></td>
    <td><c:out value="${planDates[planItemId]}"/></td>     
    <td><c:out value="${replanDates[planItemId]}"/></td>
    <td>
        <c:if test="${forecastDates[planItemId] != 'Cannot calculate'}">
            <c:out value="${forecastDates[planItemId]}"/>
        </c:if>
    </td>

</tr>


</tbody>




-->