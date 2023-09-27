<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<title>Process Phases</title>
<style>
    table { empty-cells: show; }
</style>
</head>
<body>

<h2>Defect Log Query</h2>

<table border>

<tr>
<th>Process</th>
<th>Ordinal</th>
<th>ShortName</th>
<th>TypeName</th>
</tr>

<c:forEach var="processdef"
    items="${pdash.query['select distinct pi.phase.process, pi.phase.ordinal, pi.phase.shortName, pi.phase.typeName from PlanItem as pi order by 1,2']}">

<tr>
    <td><c:out value="${processdef[0]}"/></td>
    <td><c:out value="${processdef[1]}"/></td>
    <td><c:out value="${processdef[2]}"/></td>
    <td><c:out value="${processdef[3]}"/></td>        
</tr>

</c:forEach>

</table>

</body>
</html>
