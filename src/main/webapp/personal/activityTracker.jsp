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
<link rel="stylesheet" href="https://cdn.datatables.net/fixedcolumns/4.3.0/css/fixedColumns.dataTables.min.css" />

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/4.3.0/js/dataTables.fixedColumns.min.js"></script>

<script src="./../script/activityTracker.js" ></script>
<link   href="./../style/master.css" rel="stylesheet" />


<!-- WIP - STYLING TO GET IT TO LOOK OK:

1. Question of column widths - do we redo each time we add/remove cols?
2. position of search boxes - px or percent?
3. Page number position?
3. table container size?

-->

<style>

/* Force width of timer table: */
#timerTable_Wrapper{
  width:1000px;
}

#timerTable{
  width:1000px;
}

/* Vertical spacing of "length" dropdown */
#timerTable_length{
  margin:0px 0px 15px 0px;  
}

/* Vertical spacing of "filter" dropdown */
#timerTable_filter{
  margin:0px 0px 15px 0px; 
}

/* Enable pointer for left pair of cols (path, notes) */
#timerTable_wrapper .dtfc-fixed-left {
  cursor: pointer;
}

#notesPanel{
  position: fixed; /* Stays fixed relative to viewport*/
  border-style: solid;
  border-color: rgb(11,65,145); /*BLUE */
  border-width: 1px;
  background-color: rgba(210, 222, 241); /* light grey */   
  margin:0px 0px 15px 00px;
  visibility: hidden;
  left: 200px;
  top: 400px;
  width: 600px;
  height: 200px;
  z-index: 1;
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
  <!-- TODO - NEEDS TO BE BASED ON A LISTENER... -->
  <p id="currentTask" class="currentTask" onclick="currentTaskClick()">None</p>

  <h4>Direct time tasks</h4>

  <!-- Checkboxes to show/hide rows in task table: -->
  <div id="showCompletedTasksBox" class="checkBoxFormat">
    <h4 class="checkBoxTitleFormat">Rows</h4>
    <label for="cbShowCompleted">Completed:</label>
    <input type="checkbox" id="cbShowCompleted" onclick="toggleTaskStatus()" />
    <label for="cbShowTodo">Todo:</label>
    <input type="checkbox" id="cbShowTodo" onclick="toggleTaskStatus()" />
    <label for="cbShowWip">WIP:</label>
    <input type="checkbox" id="cbShowWip" onclick="toggleTaskStatus()" />  
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

<!-- TODO - CHECK OPTIONS CORRECT -->
  <table id="timerTable" class="display compact nowrap hover cell-border"  ></table>
  <div   id="pageLoader" class="loader"></div>
  <div   id="notesPanel" onclick="hideNotesPanel()"></div>


  <h4>Overhead tasks</h4>
  <!-- TODO - overhead table -->
  <div id="overheadTasks" class="overheadTasks" >
    <button type="button" onclick="btn_Click('/LUK/OH/Admin')">Admin</button>
    <button type="button" onclick="btn_Click('/EXT/Lunch')">Lunch</button>
  </div>

</div>
</body>
</html>