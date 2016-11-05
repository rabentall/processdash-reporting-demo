<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>HQL Query</title>
<style>
table { border-collapse: collapse }
td { border: 1px solid silver; padding: 2px 5px }
div#sql.collapsed div    { display:none }
div#sql.expanded  p#link { display:none }
div#sqlContent { font-family: monospace; margin-left: 1cm; width: 50% }
</style>
</head>
<body>

<h1>HQL Query</h1>

<form action="hql.jsp" method="post">

Enter an HQL Query:<br/>
<textarea name="q" rows=10 cols=60><c:out value="${param.q}"/></textarea>

<br/><input type="submit" name="run" value="Execute Query"/>

</form>

<c:if test="${not empty param.q}">

<h2>Results</h2>

<c:catch var="queryException">
<c:set var="results" value="${pdash.query[param.q]}"/>
</c:catch>

<c:choose>

<c:when test="${queryException != null}">
<pre><c:out value="${queryException.message}"/></pre>
</c:when>

<c:when test="${empty results}">
<p><i>No items found</i></p>
</c:when>

<c:otherwise>

<table>

<c:forEach items="${results}" var="row">
<tr>

<c:catch var="exception">
<c:forEach items="${row}" var="cell">
<td><c:out value="${cell}"/></td>
</c:forEach>
</c:catch>

<c:if test="${exception != null}">
<td><c:out value="${row}"/></td>
</c:if>

</tr>

</c:forEach>

</table>

</c:otherwise>
</c:choose>


<%--
  Note: "lastSql" functionality was added in Process Dashboard version 2.1.3.
  The block of code below will generate no output on earlier versions.  To
  see raw SQL statements, please install the latest version of the dashboard.
--%>

<c:catch var="lastSqlException">
<c:set var="sql" value="${pdash.query.lastSql}"/>
</c:catch>

<c:if test="${not empty sql}">
<div id="sql" class="collapsed">

<p id="link" style="margin-top:1em">
<a href="#" onclick="this.parentNode.parentNode.className = 'expanded'; return false;"><i>Show raw SQL...</i></a>
</p>

<div>
<h2>Raw SQL</h2>
<p>Hibernate executed the following SQL statements during the execution of your query:</p>
<div id="sqlContent"><c:out value="${sql}"/></div>
</div>
&nbsp;
</div>
</c:if> <%-- not empty sql --%>

</c:if> <%-- not empty param.q --%>

</body>
</html>
