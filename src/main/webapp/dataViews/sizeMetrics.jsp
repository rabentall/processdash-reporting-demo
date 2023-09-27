<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
<title>Size Metrics</title>
<style>
    table { empty-cells: show; }
</style>
</head>
<body>

<h1><c:out value="${pdash.projectPath}"/></h1>

<h2>Size Metrics</h2>

<table border>

<tr>
<th>ProjectId</th>
<th>WbsElementId</th>
<th>SizeMetric</th>
<th>MeasurementType</th>
<th>AddedAndModifiedSize</th>
</tr>


<c:forEach var="sizeMetric" items="${pdash.query['
select
    size.planItem.project.key,
    size.planItem.wbsElement.key,
    size.sizeMetric.shortName,
    size.measurementType.name,
    sum(size.addedAndModifiedSize)
from SizeFact as size
group by size.planItem.project.key, size.planItem.wbsElement.key, size.sizeMetric.shortName, size.measurementType.name
']}">



<tr>
<td><c:out value="${sizeMetric[0]}"/></td>
<td><c:out value="${sizeMetric[1]}"/></td>
<td><c:out value="${sizeMetric[2]}"/></td>
<td><c:out value="${sizeMetric[3]}"/></td>
<td><c:out value="${sizeMetric[4]}"/></td>

</tr>

</c:forEach>

</table>

</body>
</html>
