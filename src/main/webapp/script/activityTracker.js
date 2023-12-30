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
An array of tasks returned from the jsonviews API. Provides access to task plan/actual effort, 
plan/actual/replan/forecast dates, custom cols, milestones etc.
*/
const taskDetails_ = new Map();

const tasksV2_ = new Map();

const directHoursRoot_ = "/DT/";
const overheadRoot_    = "/OH/";
const offWorkRoot_     = "/EXT/";

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
  await getTaskDetails();
  await getTaskListUsingTimer();
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
    data: Array.from(tasksV2_.values()),
    "initComplete": function(settings, json) {
      //Hide spinner when table load completed:
      document.getElementById("pageLoader").style.display = "none";
    }
  });

  var currentTaskTable = new DataTable('#currentTaskTable',
  {
     columns: [
       {width:  50}, //Must add up to 1000 (width of enclosing block)
       {width: 950}
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

      btn_Click(currentActiveTaskPath);

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

  //TODO - set up a polling loop for periodically exporting data in-memory databases
  //TODO - root paths for overhead and direct time tasks.

}

/**
 * Returns data for the tasklist, using the jsonviews API:
 */
async function getTaskDetails(){

  taskDetails_.clear(); //Clear out any existing data in the array.

  //TODO - ROOT OF "THIS" webservice.
  //TODO - constant for url.

  try{
    const response = await fetch("http://localhost:2468//pdash-reporting-rbentall-1.0/jsonViews/tasks");
    const taskListJson = await response.json();

    taskListJson.tasks.forEach((task) => {

      //TODO - TaskDetails class.
      taskDetails_.set(
        task.planItem, 
        [
        task.planTimeHours.toFixed(2), 
        task.actualTimeHours.toFixed(2), 
        task.activityStatus,
        getNullableDateValue(task, 'planDate'),
        getNullableDateValue(task, 'replanDate'),
        getNullableDateValue(task, 'forecastDate'),
        getNullableDateValue(task, 'actualStartDate'),
        getNullableDateValue(task, 'actualCompletionDate'),
        getLabel(task.planItem),
        task.planItem
        ]);
      
    });
    console.log("**** taskDetailsSize:" + taskDetails_.size);
  } catch (error) {
    console.error("Error in getTaskDetails:", error.message);
  } 
}

async function getTaskListUsingTimer(){

  tasksV2_.clear(); //Clear out any existing map entries.

  try{
    const response = await fetch("http://localhost:2468/api/v1/tasks/");
    const timerTasksJson = await response.json();

    timerTasksJson.tasks.forEach((task) => {

      var planItem = task.project.fullName + "/" + task.fullName;

      let noteText = getNote(planItem);
      let elipsis = (noteText != "") ? "..." : "";      

      if(planItem.startsWith(directHoursRoot_) || planItem.startsWith(overheadRoot_) || planItem.startsWith(offWorkRoot_)){
        tasksV2_.set(
          planItem,
          [
          task.id, 
          planItem,
          elipsis,
          getTaskDetail(planItem, 0, ""),
          getTaskDetail(planItem, 1, ""),
          getTaskDetail(planItem, 2, "OTHER"), //IsComplete - used for "overhead" or other non-dt tasks.
          getTaskDetail(planItem, 3, ""),
          getTaskDetail(planItem, 4, ""),
          getTaskDetail(planItem, 5, ""),
          getTaskDetail(planItem, 6, ""),
          getTaskDetail(planItem, 7, ""),
          getTaskDetail(planItem, 8, ""),
          noteText
        ]);
        //console.log("** INCLUDE:" + planItem);
      }else{
        //console.log("** EXCLUDE:" + planItem);
      }
    });
  } catch (error) {
    console.error("Error in getTaskMap:", error.message);
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

function getLabel(planItem){
  if(labels_.has(planItem)){
    return labels_.get(planItem);
  } else{
    return "";
  }
}

function getNote(planItem){
  if(notes_.has(planItem)){
    return notes_.get(planItem);
  } else{
    return "";
  }
}

//FIXME - CLASS
function getTaskDetail(planItem, attributeIndex, defaultReturnValue){
  if(taskDetails_.has(planItem)){
    return taskDetails_.get(planItem)[attributeIndex];
  } else{
    return defaultReturnValue;
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

        if(labels_.has(customColumn.planItem)){
          var oldValue = labels_.get(customColumn.planItem);
          labels_.set(customColumn.planItem, oldValue + ";" + thisValue);
        } else{
          labels_.set(customColumn.planItem, thisValue);
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

      notes_.set(note.planItem, note.note);      

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
  
  taskId = tasksV2_.get(taskPath)[0]; //Lookup from PlanItem path.

  if(tasksV2_.has(taskPath)){
    setTimer(taskId, true);
  }else{
    console.error("Key missing from tasksV2_:" + taskId);
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
  document.getElementById("cbShowOther").checked = false;

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
 *  - OTHER
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
    sep = "|";    
  }  

  if(document.getElementById("cbShowOther").checked){
    taskStatus = taskStatus + sep + "OTHER";
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