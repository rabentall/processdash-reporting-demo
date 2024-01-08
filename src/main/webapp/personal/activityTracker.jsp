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

<script src="./../script/dataLoader.js" ></script>
<script src="./../script/activityTracker.js" ></script>
<link   href="./../style/master.css" rel="stylesheet" />

<style>

  .selectionContainer{
    width: 450px;
    margin:0px 0px 15px 00px;
  }

  .checkBoxFormat{
    font-weight: 100;
    color: rgb(11,65,145); /* BLUE */
    background-color: rgba(210, 222, 241); /* light grey */
    padding: 5px 5px 5px 5px;
    margin: 0px 0px 0px 0px;
  }

  .buttonContainer{
    font-weight: 100;
    color: rgb(11,65,145); /* BLUE */
    padding: 5px 5px 5px 5px;
    margin: 0px 0px 0px 0px;
    margin:0px 0px 15px 00px;
  }



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

  <div id="buttonBar" class="buttonContainer" >
    <button type="button" onclick="btn_Pause()"         id="buttonPause"    >Pause</button>
    <button type="button" onclick="btn_MarkComplete()"  id="buttonComplete" >Mark complete</button>
    <button type="button" onclick="btn_ClickOverhead()" id="buttonAdmin"    >Admin</button>
    <button type="button" onclick="btn_ClickOffWork()"  id="buttonOffWork"  >Off work</button>
  </div>

  <h4>Available tasks</h4>

  <!-- Checkboxes to show/hide rows in task table: -->
  <div class="selectionContainer">
    <div id="horizonBox" class="checkBoxFormat">
      <fieldset>
        <legend>Horizon</legend>
        <label for="cbHorizonLTE14">2 Weeks</label>
        <input type="checkbox" id="cbHorizonLTE14" onclick="toggleHorizon()" />
        <label for="cbHorizonLTE35">2 - 5 Weeks</label>
        <input type="checkbox" id="cbHorizonLTE35" onclick="toggleHorizon()" />
        <label for="cbHorizonGT35">&GT; 5 Weeks</label>
        <input type="checkbox" id="cbHorizonGT35"  onclick="toggleHorizon()" />
      </fieldset>
    </div>
    <div id="showCompletedTasksBox" class="checkBoxFormat">
      <fieldset >
        <legend>Status</legend>
        <label for="cbShowCompleted">Completed:</label>
        <input type="checkbox" id="cbShowCompleted" onclick="toggleTaskStatus()" />
        <label for="cbShowTodo">Todo:</label>
        <input type="checkbox" id="cbShowTodo" onclick="toggleTaskStatus()" />
        <label for="cbShowWip">WIP:</label>
        <input type="checkbox" id="cbShowWip" onclick="toggleTaskStatus()" />
        <label for="cbShowOther">Other:</label>
        <input type="checkbox" id="cbShowOther" onclick="toggleTaskStatus()" />
      </fieldset>
    </div>
    <div   id="showColumnsBox" class="checkBoxFormat">
      <fieldset>
        <legend>Columns</legend>
        <label for="cbShowDates">Dates:</label>
        <input type="checkbox" id="cbShowDates" onclick="toggleColumnStatus()" />
        <label for="cbShowHours">Hours:</label>
        <input type="checkbox" id="cbShowHours" onclick="toggleColumnStatus()" />
        <label for="cbShowLabels">Labels:</label>
        <input type="checkbox" id="cbShowLabels" onclick="toggleColumnStatus()" />
      </fieldset>
    </div>
  </div>

  <table id="timerTable" class="nowrap  hover row-border compact cell-border"  ></table>
  <div   id="pageLoader" class="loader"></div>
  <div   id="notesPanel" onclick="hideNotesPanel()"></div>
</div>
</body>
</html>