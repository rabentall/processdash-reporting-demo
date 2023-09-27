<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
<title>Task List Query</title>
<style>
    table { empty-cells: show; }
</style>
</head>
<body>

<h1><c:out value="${pdash.projectPath}"/></h1>

<h2>Task List Query</h2>

<table border>

<tr>
<th>Project/Task</th>
<th>Type</th>
<th>Plan Time</th>
<th>Actual Time</th>
<th>Percent Spent</th>
<th>Date Completed</th>
<th>Assigned To</th>
</tr>

<c:forEach var="task" items="${pdash.query['from TaskStatusFact as t']}">

<tr>

<td><c:out value="${task.planItem}"/></td>
<td><c:out value="${task.planItem.phase.shortName}"/></td>
<td><fmt:formatNumber value="${task.planTimeMin}"/></td>
<td><fmt:formatNumber value="${task.actualTimeMin}"/></td>
<td><c:if test="${task.planTimeMin > 0}"><fmt:formatNumber type="percent"
     value="${task.actualTimeMin / task.planTimeMin}"/></c:if></td>
<td><c:out value="${task.actualCompletionDateDim}"/></td>
<td><c:out value="${task.dataBlock.person}"/></td>
</tr>

</c:forEach>

</table>

</body>
</html>
