//
//
// Populates map of task IDs + task paths in personal dashboard
//
//FIXME - Duplicate code
//FIXME - Overhead table
//FIXME - Table width.
//FIXME - Timeouts
//TODO - WBSELement notes? how?


/*
Contains timer status as retrieved from the personal data timer API.
*/
var timerJson_;

/*
An array of wbselements returned from the jsonViews API. Provides access to component status (completed/wip/todo).
*/
var wbsElements_ = new Map();

/*
An array of labels returned from jsonViews API. Provides access to labels that can be displayed on tasklist. 
*/
var labels_ = new Map();

/*
An array of notes returned from jsonViews API.
*/
var notes_ = new Map();

/*
A map of tasks returned from the personal data tasks API. Use to identify the task ID needed to start/stop the timer.
*/
const timerTaskMap_ = new Map(); 

/*
An array of tasks returned from the jsonviews API. Provides access to task plan/actual effort, 
plan/actual/replan/forecast dates, custom cols, milestones etc.
*/
const tasks_ = new Array();

/*
An array of key-value pairs used to display current task status.
*/
const currentTaskInfo_ = new Array();

/*
  Col indices for timer table:
*/
const COL_IX_PLAN_ITEM_ID = 0;
const COL_IX_PLAN_ITEM    = 1;
const COL_IX_NOTES = 2;
const COL_IX_PLAN_TIME_HOURS = 3;
const COL_IX_ACTUAL_TIME_HOURS = 4;
const COL_IX_ACTIVITY_STATUS = 5;
const COL_IX_PLAN_DATE = 6;
const COL_IX_REPLAN_DATE = 7;
const COL_IX_FORECAST_DATE = 8;
const COL_IX_START_DATE = 9;
const COL_IX_END_DATE = 10;
const COL_IX_LABELS = 11;
const COL_IX_NOTES_CONTENT = 12;

/*
Row indices for current task table:
*/

const ROW_IX_CURRENT_TASK = 0;
const ROW_IX_ESTIMATED_HOURS = 1;
const ROW_IX_ACTUAL_HOURS = 2;

/*
  Initialises all data needed for the page, then renders the tables.
*/
async function initTaskListTable(){

  //Initialise checkboxes:
  initCheckboxes();

  /**
   * Initialise data arrays from webservices:
   */
  await getLabels();
  await getNotes();
  await getWbsElements();
  await getTaskList();
  await getTimerTaskmap();
  await getcurrentTaskInfo();

  /**
   * Build the table:
   */
  var timerTable = new DataTable('#timerTable', {
    columns: [
      { title: 'Key',        visible: false},
      { title: 'PlanItem',   visible: true, width: 200},
      { title: '...',        visible: true, width: 10},  
      { title: 'Plan(Hrs)',  visible: false},
      { title: 'Act(Hrs)',   visible: false},
      { title: 'IsComplete', visible: false},
      { title: 'Plan',       visible: false},
      { title: 'Replan',     visible: false},
      { title: 'Forecast',   visible: false},
      { title: 'Start',      visible: false},
      { title: 'End',        visible: false},
      { title: 'Labels',     visible: false},
      { title: 'Notes',      visible: false}
    ],
    "autoWidth": false,
    fixedColumns: { left: 2 },
    scrollX: true,
    data: tasks_,
    "initComplete": function(settings, json) {
      //Hide spinner when table load completed:
      document.getElementById("pageLoader").style.display = "none";
    }
  });

  var currentTaskTable = new DataTable('#currentTaskTable',
  {
     columns: [
       {width: 100},
       {width: 200}
    ],
    info: false,
    ordering: false,
    paging: false,
    searching: false,
    autoWidth: false,
    data: currentTaskInfo_
});

  //Initialise table row/column visibility:
  toggleTaskStatus(); 
  toggleColumnStatus();

  //Handle change of cursor when we have notes to view or a timer path to click:
  timerTable.on('mouseenter', 'tbody td', function(){

    let colIndex = this.cellIndex;
    if(colIndex == 1){

      let noteText =timerTable.row(this).data()[COL_IX_NOTES_CONTENT];
      if(noteText != ""){
        document.getElementsByTagName("body")[0].style.cursor = "pointer";        
      }
    } else if(colIndex == 0){
      document.getElementsByTagName("body")[0].style.cursor = "pointer";              
    }    
  })

  //Handle change of cursor when we have notes to view or a timer path to click:  
  timerTable.on('mouseleave', 'tbody td', function(){

    let colIndex = this.cellIndex;
    if(colIndex == 0 || colIndex == 1){
      document.getElementsByTagName("body")[0].style.cursor = "default";
    }    
  })

  //Handle clicking on a cell in the timer table. Only the first two cols
  //respond:
  timerTable.on('click', 'tbody td', function() {

    let noteVisibility = document.getElementById("notesPanel").style.visibility;

    let colIndex = this.cellIndex;

    //planItem column:
    if(colIndex == 0){
 
      //Hide notes if currently showing:
      if(noteVisibility == "visible"){
        document.getElementById("notesPanel").style.visibility = "hidden";        
      }

      var currentActiveRowReference = $(this).parent('tr');
      let currentActiveTaskPath =  currentActiveRowReference.children()[0].innerHTML;

      //Get the activeTaskId from the timerTaskMap lookup.
      let activeTaskId = timerTaskMap_.get(currentActiveTaskPath);

      //If we have a valid taskId, set the timer to run:
      if(timerTaskMap_.has(currentActiveTaskPath)){
        setTimer(activeTaskId, true);
      }else{
        console.error("Key missing from timerTaskMap_:" + activeTaskId);
      }     

    //notes elipsis (...)  
    } else if(colIndex == 1){

      //Show notes + don't toggle timer.
      //The first time this happens, the visibility property evaluates to an empty string.
      //Notes panel is hidden when we click on it or hit escape.
      if(noteVisibility == "" || noteVisibility == "hidden"){

        let noteText = timerTable.row(this).data()[COL_IX_NOTES_CONTENT];

        if(noteText != ""){
          document.getElementById("notesPanel").innerHTML = noteText;
          document.getElementById("notesPanel").style.visibility = "visible";
        }
      }else{
        document.getElementById("notesPanel").style.visibility = "hidden";
      }
      
    }else{
      //Do nothing....
    }
  })

  //Set up a polling loop for the current task at 100ms:
  setInterval(updateTimerStatus, 100);

}

/**
 * Returns data for the tasklist, using the jsonviews API:
 */
async function getTaskList(){

  tasks_.length = 0; //Clear out any existing data in the array.

  try{
    const response = await fetch("http://localhost:2468//pdash-reporting-rbentall-1.0/jsonViews/tasks");
    const taskListJson = await response.json();

    taskListJson.tasks.forEach((task) => {

      let noteText = getNote(task.planItemId);
      let elipsis = (noteText != "") ? "..." : "";

      tasks_.push([
        task.planItemId,
        task.planItem, 
        elipsis,         
        task.planTimeHours.toFixed(2), 
        task.actualTimeHours.toFixed(2), 
        task.activityStatus,
        getNullableDateValue(task, 'planDate'),
        getNullableDateValue(task, 'replanDate'),
        getNullableDateValue(task, 'forecastDate'),
        getNullableDateValue(task, 'actualStartDate'),
        getNullableDateValue(task, 'actualCompletionDate'),
        getLabel(task.planItemId),
        noteText 
        ]);
      
    });
  } catch (error) {
    console.error("Error in getTaskList:", error.message);
  } 
}

async function getcurrentTaskInfo(){

  currentTaskInfo_.length = 0; //Clear out any existing data in the array.

  try{

    currentTaskInfo_.push(["Current task",  "TODO"]);
    currentTaskInfo_.push(["Estimated hrs", "TODO"]);
    currentTaskInfo_.push(["Actual hrs",    "TODO"]);
      
  } catch (error) {
    console.error("Error in getcurrentTaskInfo:", error.message);
  } 
}

function getNullableDateValue(obj, prop){
  if(obj.hasOwnProperty(prop)){
    var dateVal = new Date(obj[prop]);
    return dateVal.toISOString().split('T')[0];
  }else{
  return "";
  }
}

function getLabel(planItemId){
  if(labels_.has(planItemId)){
    return labels_.get(planItemId);
  } else{
    return "";
  }
}

function getNote(planItemId){
  if(notes_.has(planItemId)){
    return notes_.get(planItemId);
  } else{
    return "";
  }
}

/**
 * Returns data for the timer task map, using the personal data tasks API:
 */
async function getTimerTaskmap(){

  timerTaskMap_.clear(); //Clear out any existing map entries.

  try{
    const response = await fetch("http://localhost:2468/api/v1/tasks/");
    const timerTasksJson = await response.json();

    timerTasksJson.tasks.forEach((task) => {
      timerTaskMap_.set(task.project.fullName + "/" + task.fullName, task.id);
    });
  } catch (error) {
    console.error("Error in getTaskMap:", error.message);
  }    
}

/**
 * Returns data for looking up component-level status. 
 */
async function getWbsElements(){

  wbsElements_.clear(); //Clear out any existing data in the array.

  try{
    const response = await fetch("http://localhost:2468//pdash-reporting-rbentall-1.0/jsonViews/wbsElements");
    const wbsElementsJson = await response.json();

    wbsElementsJson.wbsElements.forEach((wbsElement) => {
      wbsElements_.set(wbsElement.project + "/" + wbsElement.wbsElement, wbsElement.activityStatus);
    });
  } catch (error) {
    console.error("Error in getWbsElements:", error.message);
  } 
}

async function getLabels(){

  labels_.clear(); //Clear out any existing data in the array.

  try{
    const response = await fetch("http://localhost:2468//pdash-reporting-rbentall-1.0/jsonViews/customColumns");
    const labelsJson = await response.json();

    labelsJson.customColumns.forEach((customColumn) => {

      if(customColumn.name == 'Label'){

        var thisValue = customColumn.value;

        if(labels_.has(customColumn.planItemId)){
          var oldValue = labels_.get(customColumn.planItemId);
          labels_.set(customColumn.planItemId, oldValue + ";" + thisValue);
        } else{
          labels_.set(customColumn.planItemId, thisValue);
        }
      }
    });
  } catch (error) {
    console.error("Error in getLabels:", error.message);
  } 
}


async function getNotes(){

  notes_.clear(); //Clear out any existing data in the array.

  try{
    const response = await fetch("http://localhost:2468//pdash-reporting-rbentall-1.0/jsonViews/notes");
    const notesJson = await response.json();

    notesJson.notes.forEach((note) => {

      notes_.set(note.planItemId, note.note);      

    });
  } catch (error) {
    console.error("Error in getLabels:", error.message);
  } 
}

async function updateTimerStatus(){

  try{
    const response = await fetch("http://localhost:2468/api/v1/timer/");
    timerJson_ = await response.json();

    let activeTask = timerJson_.timer.activeTask;
    let timerTaskPath = activeTask.project.fullName + "/" + activeTask.fullName;

    let estimatedHrs = activeTask.estimatedTime / 60.0;
    let actualHrs = activeTask.actualTime / 60.0;

    //Update the current task table:
    var currentTaskTable = new DataTable('#currentTaskTable');    
    currentTaskTable.cell(ROW_IX_CURRENT_TASK,    1).data(timerTaskPath);
    
    currentTaskTable.cell(ROW_IX_ESTIMATED_HOURS, 1).data(estimatedHrs.toFixed(2));
    currentTaskTable.cell(ROW_IX_ACTUAL_HOURS,    1).data(actualHrs.toFixed(2));

    //Handles highlighting of current task path contents:
    var currentTaskPathCell = currentTaskTable.cell(ROW_IX_CURRENT_TASK,    1).node();
    $(currentTaskPathCell).addClass('currentTask');

    if(timerJson_.timer.timing){
      currentTaskTable.cell(ROW_IX_CURRENT_TASK,    1).data(timerTaskPath);
    }else{
      currentTaskTable.cell(ROW_IX_CURRENT_TASK,    1).data(timerTaskPath + " [PAUSED]");      
    }

  } catch (error) {
    console.error("Error in getTimerStatus:", error.message);
  }      
}

function setTimer(activeTaskId, timing) {

  const requestOptions = {
      method: 'PUT',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      body:  'activeTaskId=' + activeTaskId + '&timing=' + timing
  };

  fetch('http://localhost:2468/api/v1/timer/', requestOptions)
      .then(response => response)
      .catch(error => {
          console.error('There was an error!', error);
      });
   
}

function getWbsElementStatus(wbsElementPath){
  if(wbsElements_.has(wbsElementPath)){
    const wbsElementStatus = wbsElements_.get(wbsElementPath);  
    return wbsElementStatus;    
  }else{
    console.error("Key missing from wbsElements_:" + wbsElementPath);
  } 

}

async function btn_Click(taskPath){
  
  taskId = timerTaskMap_.get(taskPath); //Lookup from PlanItem path.

  if(timerTaskMap_.has(taskPath)){
    setTimer(taskId, true);
  }else{
    console.error("Key missing from timerTaskMap_:" + taskId);
  }
}

/*
  Pause the current task.
*/
async function btn_Pause(){
  setTimer(timerJson_.timer.activeTask.id, false);
}

/**
 * On page load, we have WIP tasks showing with planItem column only.
 */
function initCheckboxes(){

  //Rows:
  document.getElementById("cbShowCompleted").checked = false;
  document.getElementById("cbShowTodo").checked = false;
  document.getElementById("cbShowWip").checked = true;

  //Columns:
  document.getElementById("cbShowDates").checked = false;
  document.getElementById("cbShowHours").checked = true;
  document.getElementById("cbShowLabels").checked = true;
}

/**
 * Use to apply filter to "activitystatus" column. Contents of this column are an enum with values
 *  - COMPLETED
 *  - TODO
 *  - WIP
 */
function toggleTaskStatus(){

  var timerTable = new DataTable('#timerTable');

  let taskStatus = "";
  let sep = "";

  if(document.getElementById("cbShowCompleted").checked){
    taskStatus = taskStatus + "COMPLETED";
    sep = "|";
  }

  if(document.getElementById("cbShowTodo").checked){
    taskStatus = taskStatus + sep + "TODO";
    sep = "|";
  }

  if(document.getElementById("cbShowWip").checked){
    taskStatus = taskStatus + sep + "WIP";
  }  

  timerTable.column(COL_IX_ACTIVITY_STATUS).search(taskStatus, true).draw();

}

/*
  Use to show/hide columns in the timer table
*/
function toggleColumnStatus(){

  var timerTable = new DataTable('#timerTable');

  var datesVisible = document.getElementById("cbShowDates").checked;
  timerTable.column(COL_IX_PLAN_DATE).visible(datesVisible);
  timerTable.column(COL_IX_REPLAN_DATE).visible(datesVisible);
  timerTable.column(COL_IX_FORECAST_DATE).visible(datesVisible);
  timerTable.column(COL_IX_START_DATE).visible(datesVisible);
  timerTable.column(COL_IX_END_DATE).visible(datesVisible);

  var hoursVisible = document.getElementById("cbShowHours").checked;
  timerTable.column(COL_IX_PLAN_TIME_HOURS).visible(hoursVisible);  
  timerTable.column(COL_IX_ACTUAL_TIME_HOURS).visible(hoursVisible);  

  var labelsVisible = document.getElementById("cbShowLabels").checked;
  timerTable.column(COL_IX_LABELS).visible(labelsVisible);

  timerTable.columns.adjust().draw();
}

function hideNotesPanel(){
  document.getElementById("notesPanel").style.visibility = "hidden";

}

function shortcutEventHandler(event){
  if(event.key=="Escape"){
    document.getElementById("notesPanel").style.visibility = "hidden";
  }
}