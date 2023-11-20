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




/*
Contains timer status as retrieved from the personal data timer API.
*/
var timerJson_;

/*
An array of wbselements returned from the jsonviews API. Provides access to component status (completed/wip/todo).
*/
var wbsElements_ = new Map();

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

  //Set up a polling loop for the current task:
  setInterval(updateTimerStatus, 1000);

  /**
   * Initialise data arrays from webservices:
   */
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
      { title: 'PlanTimeHours' },
      { title: 'ActualTimeHours' },
      { title: 'IsComplete'}
    ],
    data: tasks_,
    "initComplete": function(settings, json) {
      //Hide spinner when table load completed:
      document.getElementById("pageLoader").style.display = "none";
    }
  });

  /**
   * Event handler for row onclick event.
   */
  timerTable.on('click', 'tbody tr', function () {

    let activeTaskPath = timerTable.row(this).data()[1];
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
      tasks_.push([task.planItemId, task.planItem, task.planTimeHours, task.actualTimeHours, getWbsElementStatus(task.project + '/' + task.wbsElement)]);
    });
    console.log("CountOfTaskList:" + tasks_.length);

  } catch (error) {
    console.error("Error in getTaskList:", error.message);
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


async function updateTimerStatus(){

  try{
    const response = await fetch("http://localhost:2468/api/v1/timer/");
    timerJson_ = await response.json();

    if(timerJson_.timer.timing){

      let activeTask = timerJson_.timer.activeTask;
   
      document.getElementById("currentTask").innerHTML =  "/" + activeTask.project.fullName + "/" + activeTask.fullName;


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

function toggleTaskStatus(){
  console.log("toggleTaskStatus");
  console.log("cbShowCompleted" + document.getElementById("cbShowCompleted").checked);
  console.log("cbShowTodo" + document.getElementById("cbShowTodo").checked);
  console.log("cbShowWip" + document.getElementById("cbShowWip").checked);

  //TODO - SET FILTER
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