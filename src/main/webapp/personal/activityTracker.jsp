<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<html>
<head>
<title>Activity Tracker</title>

<link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.css" />
<link rel="stylesheet" href="https://cdn.datatables.net/fixedcolumns/4.3.0/css/fixedColumns.dataTables.min.css" />

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/4.3.0/js/dataTables.fixedColumns.min.js"></script>

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

<body onkeyup="shortcutEventHandler(event);">

<h3  class="h3Content" >Activity Tracker</h3>
<div class="contentBody">

  <h4>Current task</h4>  

  <table id="currentTaskTable" class=" nowrap cell-border compact " ></table>

  <!-- FIXME - 1 - what buttons? 2 - defaults + paths? 3 - styles -->
  <div id="overheadTasks" class="overheadTasks" >
    <button type="button" onclick="btn_Click('/OH/Admin')">Admin</button>
    <button type="button" onclick="btn_Pause()">Pause</button>       
    <button type="button" onclick="btn_Click('/EXT/Off work')">Off work</button>
  </div>

  <h4>Available tasks</h4>

  <!-- Checkboxes to show/hide rows in task table: -->
  <div id="showCompletedTasksBox" class="checkBoxFormat">
    <h4 class="checkBoxTitleFormat">Rows</h4>
    <label for="cbShowCompleted">Completed:</label>
    <input type="checkbox" id="cbShowCompleted" onclick="toggleTaskStatus()" />
    <label for="cbShowTodo">Todo:</label>
    <input type="checkbox" id="cbShowTodo" onclick="toggleTaskStatus()" />
    <label for="cbShowWip">WIP:</label>
    <input type="checkbox" id="cbShowWip" onclick="toggleTaskStatus()" />  
    <label for="cbShowOther">Other:</label>
    <input type="checkbox" id="cbShowOther" onclick="toggleTaskStatus()" />      
  </div>
  <!-- Checkboxes to show/hide columns in task table: -->  
  <div   id="showColumnsBox" class="checkBoxFormat">
    <h4 class="checkBoxTitleFormat">Columns</h4>    
    <label for="cbShowDates">Dates:</label>
    <input type="checkbox" id="cbShowDates" onclick="toggleColumnStatus()" />
    <label for="cbShowHours">Hours:</label>
    <input type="checkbox" id="cbShowHours" onclick="toggleColumnStatus()" />
    <label for="cbShowLabels">Labels:</label>
    <input type="checkbox" id="cbShowLabels" onclick="toggleColumnStatus()" />
  </div>

  <table id="timerTable" class="nowrap  hover row-border compact cell-border"  ></table>
  <div   id="pageLoader" class="loader"></div>
  <div   id="notesPanel" onclick="hideNotesPanel()"></div>
</div>
</body>
</html>