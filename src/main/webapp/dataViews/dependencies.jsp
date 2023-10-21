<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
<title>Dependencies</title>
<style>
    table { empty-cells: show; }
</style>
</head>
<body>

<h1><c:out value="${pdash.projectPath}"/></h1>

<h2>Dependencies</h2>

<table border>

<tr>
<th>Predecessor</th>
<th>Successor</th>
</tr>


<c:set var="rows" value="${pdash.query['
    select 
        dep.predecessor.id, 
        dep.successor.id
    from PlanItemDependencyFact as dep']}"/>

<c:forEach var="row" items="${rows}">
    <tr>
        <td><c:out value="${row[0]}"/></td>
        <td><c:out value="${row[1]}"/></td>
    </tr>
</c:forEach>

</table>
</body>
</html>