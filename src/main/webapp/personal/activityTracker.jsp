<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<html>
<head>
<title>Activity Tracker</title>
<!-- FIXME USE LOCAL -->


<!-- <link rel="stylesheet" href="./../script/libs/DataTables/1.13.8/css/jquery.dataTables.css" />
<script src="./../script/libs/jquery/3.7.0/jquery.min.js"></script>
<script src="./../script/libs/DataTables/1.13.8/js/jquery.dataTables.js"></script> -->



<link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.js"></script>

<script src="./../script/activityTracker.js" ></script>
<link   href="./../style/master.css" rel="stylesheet" />

<style>

</style>
</head>

<script>
//
// Document ready handler:
//
$(document).ready(() =>
{
  initTaskListTable();
});

</script>

<body>

<h3  class="h3Content" >Activity Tracker</h3>
<div class="contentBody">

  <h4>Current task</h4>
  <!-- TODO - NEEDS TO BE BASED ON A LISTENER... -->
  <p id="currentTask" class="currentTask" onclick="currentTaskClick()">None</p>

  <h4>Direct time tasks</h4>

  <!-- TODO - width of table -->
  <div   id="pageLoader" class="loader"></div>


  <table id="timerTable" class="compact hover" style="width:100%"></table>

  <!-- TODO - WIP/Not started/Completed -->
  <div   id="showCompletedTasksBox" >
    <label for="cbShowCompleted">Completed:</label>
    <input type="checkbox" id="cbShowCompleted" onclick="toggleTaskStatus()" />
    <label for="cbShowTodo">Todo:</label>
    <input type="checkbox" id="cbShowTodo" onclick="toggleTaskStatus()" />
    <label for="cbShowWip">WIP:</label>
    <input type="checkbox" id="cbShowWip" onclick="toggleTaskStatus()" />        
  </div>

  <h4>Overhead tasks</h4>
  <!-- TODO - overhead table -->
  <div id="overheadTasks" class="overheadTasks" >
    <button type="button" onclick="btn_Click('/LUK/OH/Admin')">Admin</button>
    <button type="button" onclick="btn_Click('/EXT/Lunch')">Lunch</button>
    <!-- <button type="button" onclick="pause_Click()">Pause</button> -->

  </div>

</div>
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