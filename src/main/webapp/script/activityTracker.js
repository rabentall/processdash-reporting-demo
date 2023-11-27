//
//
// Populates map of task IDs + task paths in personal dashboard
//
//FIXME - ForwardSlash
//FIXME - Decimal places
//FIXME - Dates
//FIXME - Duplicate code
//FIXME - Overhead table
//FIXME - Table width.
//FIXME - Timeouts

//FIXME - ADD EXTRA COLS.
const COL_IX_PLAN_ITEM_ID = 0;
const COL_IX_PLAN_ITEM    = 1;
const COL_IX_PLAN_TIME_HOURS = 2;
const COL_IX_ACTUAL_TIME_HOURS = 3;
const COL_IX_ACTIVITY_STATUS = 4;



/*
Contains timer status as retrieved from the personal data timer API.
*/
var timerJson_;

/*
An array of wbselements returned from the jsonviews API. Provides access to component status (completed/wip/todo).
*/
var wbsElements_ = new Map();

/*
An array of labels returned from jsonViews API. Provides access to labels that can be displayed on tasklist. 
*/
var labels_ = new Map();

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
  Initialises all data needed for the page, then renders the tasks table.
*/
async function initTaskListTable(){

  //Initialise checkboxes:
  initCheckboxes();

  //Set up a polling loop for the current task:
  setInterval(updateTimerStatus, 1000);

  /**
   * Initialise data arrays from webservices:
   */
  await getLabels();
  await getWbsElements();
  await getTaskList();
  await getTimerTaskmap();


  /**
   * Build the table:
   */
  var timerTable = new DataTable('#timerTable', {
    columns: [
      { title: 'Key', visible: false },
      { title: 'PlanItem' },
      { title: 'Plan(Hrs)' },
      { title: 'Act(Hrs)' },
      { title: 'IsComplete', visible: false},
      { title: 'Plan'},
      { title: 'Replan'},
      { title: 'Forecast'},
      { title: 'Start'},
      { title: 'End'},
      { title: 'Labels'}
    ],
    data: tasks_,
    "initComplete": function(settings, json) {
      //Hide spinner when table load completed:
      document.getElementById("pageLoader").style.display = "none";
    }
  });

  //Initialise filter on table:
  toggleTaskStatus();  

  /**
   * Event handler for row onclick event.
   */
  timerTable.on('click', 'tbody tr', function () {

    let activeTaskPath = timerTable.row(this).data()[COL_IX_PLAN_ITEM];
    console.log("**** activeTaskPath:" + activeTaskPath);

    document.getElementById("currentTask").innerHTML = activeTaskPath;    
    
    activeTaskId = timerTaskMap_.get(activeTaskPath); //Lookup from PlanItem path.

    console.log("*** activeTaskId:" + activeTaskId);

    if(timerTaskMap_.has(activeTaskPath)){
      toggleTimer(activeTaskId);
    }else{
      console.error("Key missing from timerTaskMap_:" + activeTaskId);
    }
  });
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
      tasks_.push([
        task.planItemId, 
        task.planItem, 
        task.planTimeHours.toFixed(2), 
        task.actualTimeHours.toFixed(2), 
        task.activityStatus,
        getNullableDateValue(task, 'planDate'),
        getNullableDateValue(task, 'replanDate'),
        getNullableDateValue(task, 'forecastDate'),
        getNullableDateValue(task, 'actualStartDate'),
        getNullableDateValue(task, 'actualCompletionDate'),
        getLabel(task.planItemId)
      ]);

      console.log("__" + task.planItemId + ":" + task.hasOwnProperty('actualStartDate') + ":" + getNullableDateValue(task, 'actualStartDate')); 
      
    });
    console.log("CountOfTaskList:" + tasks_.length );

  } catch (error) {
    console.error("Error in getTaskList:", error.message);
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

/**
 * Returns data for the timer task map, using the personal data tasks API:
 */
async function getTimerTaskmap(){

  timerTaskMap_.clear(); //Clear out any existing map entries.

  try{
    const response = await fetch("http://localhost:2468/api/v1/tasks/");
    const timerTasksJson = await response.json();

    timerTasksJson.tasks.forEach((task) => {
      //console.log("**** Task:" + task.project.fullName + "/" + task.fullName);
      timerTaskMap_.set(task.project.fullName + "/" + task.fullName, task.id);
    });
    console.log("CountOftimerTaskMap_:" + timerTaskMap_.size);    
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
    console.log("CountOfwbsElements_:" + wbsElements_.size);    
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
    console.log("CountOflabels_:" + labels_.size);
  } catch (error) {
    console.error("Error in getLabels:", error.message);
  } 
}


async function updateTimerStatus(){

  try{
    const response = await fetch("http://localhost:2468/api/v1/timer/");
    timerJson_ = await response.json();

    if(timerJson_.timer.timing){

      let activeTask = timerJson_.timer.activeTask;
   
      document.getElementById("currentTask").innerHTML = activeTask.project.fullName + "/" + activeTask.fullName;


    }else{
      document.getElementById("currentTask").innerHTML = "None"; 
    }

  } catch (error) {
    console.error("Error in getTimerStatus:", error.message);
  }      
}

function toggleTimer(activeTaskId) {

  const requestOptions = {
      method: 'PUT',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      body:  'activeTaskId=' + activeTaskId + '&timing=true'
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
  
  console.log("**** taskPath:" + taskPath);
  
  taskId = timerTaskMap_.get(taskPath); //Lookup from PlanItem path.

  console.log("activeTaskId:" + taskId);

  if(timerTaskMap_.has(taskPath)){
    toggleTimer(taskId);
  }else{
    console.error("Key missing from timerTaskMap_:" + taskId);
  }

}

/**
 * On page load, we just have WIP tasks showing.
 */
function initCheckboxes(){
  document.getElementById("cbShowWip").checked = true;
}

/**
 * Use to apply filter to "activitystatus" column. contens of this column are an enup with values
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
  Provides initial setup of data tables
*/
async function getData(taskList, planDates, replanDates, forecastDates){

}

/*
  Want to create two tables:
   - Direct time tasks
   - Overhead tasks
*/
async function buildDirectTimeTable(taskList, planDates, replanDates, forecastDates, timerTasks){



}

async function buildOverheadTimeTable(timerTasks){

}



// FIXME
function currentTaskClick(){
  console.log("**** CurrentTask:Click");
  if(timerJson_.timer.timing){
    //document.getElementById("currentTask").style.borderStyle = "solid";
    //document.getElementById("currentTask").style.borderColor = "red";
    //document.getElementById("currentTask").style.borderWidth = "2px";
    console.log("**** RUNNING");
  }else{
    //document.getElementById("currentTask").style.borderStyle = "solid";
    //document.getElementById("currentTask").style.borderColor = "rgba(210, 222, 241)";
    //document.getElementById("currentTask").style.borderWidth = "2px";    
    console.log("**** STOPPED");
  }
}


function pause_Click(){
  console.log("pauseClick");
}