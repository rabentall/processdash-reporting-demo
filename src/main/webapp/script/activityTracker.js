//
//
// Populates map of task IDs + task paths in personal dashboard
//

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

var timerJson_;


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

async function getTaskMap(){
  //Get list of tasks from task API + build lookup map:
  //TIMEOUT - FUNCTION
  const taskLookup = new Map();    

  try{
    const response = await fetch("http://localhost:2468/api/v1/tasks/");
    const tasksJson = await response.json();

    tasksJson.tasks.forEach((task) => {
      console.log("**** Task:" + task.project.fullName + "/" + task.fullName);
        taskLookup.set(task.project.fullName + "/" + task.fullName, task.id);
    });
  } catch (error) {
    console.error("Error in getTaskMap:", error.message);
  }    

  return taskLookup;
}

async function getTaskList(){
  //Get list of tasks from task API:
  const taskList = new Array();    

  try{
    const response = await fetch("http://localhost:2468//pdash-reporting-rbentall-1.0/jsonViews/tasks");
    const taskListJson = await response.json();

    taskListJson.tasks.forEach((task) => {
      taskList.push([task.planItemId, task.planItem, task.planTimeHours, task.actualTimeHours, task.isComplete ]);
    });
    console.log("CountOfTaskList:" + taskList.length);

  } catch (error) {
    console.error("Error in getTaskList:", error.message);
  }    

  return taskList;
}

//FIXME - ForwardSlash
//FIXME - Decimal places
//FIXME - Dates
//FIXME - Duplicate code
//FIXME - Overhead table
//FIXME - Table width.

async function initTaskListTable(){

  //Set up a polling loop for the current task:
  setInterval(updateTimerStatus, 1000);

  const taskList = await getTaskList();

  const taskLookup = await getTaskMap();

  //TODO - FILTER TO ONLY INCLUDE OPEN TASKS
  var timerTable = new DataTable('#timerTable', {
    columns: [
      { title: 'Key', visible: false },
      { title: 'PlanItem' },
      { title: 'PlanTimeHours' },
      { title: 'ActualTimeHours' },
      { title: 'IsComplete'}
    ],
    data: taskList,
    "initComplete": function(settings, json) {
      //Hide spinner when table load completed:
      document.getElementById("pageLoader").style.display = "none";
    }
  }

  );

  timerTable.on('click', 'tbody tr', function () {

    let activeTaskPath = timerTable.row(this).data()[1];
    console.log("**** activeTaskPath:" + activeTaskPath);

    document.getElementById("currentTask").innerHTML = activeTaskPath;    
    
    activeTaskId = taskLookup.get(activeTaskPath); //Lookup from PlanItem path.

    console.log("*** activeTaskId:" + activeTaskId);

    if(taskLookup.has(activeTaskPath)){
      toggleTimer(activeTaskId);
    }else{
      console.error("Key missing from taskLookup:" + activeTaskId);
    }

  });
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


async function btn_Click(taskPath){
  
  const taskLookup = await getTaskMap(); //FIXME - DUPLICATE

  //let taskPath = ;
  console.log("**** taskPath:" + taskPath);
  
  taskId = taskLookup.get(taskPath); //Lookup from PlanItem path.

  console.log("activeTaskId:" + taskId);

  if(taskLookup.has(taskPath)){
    toggleTimer(taskId);
  }else{
    console.error("Key missing from taskLookup:" + taskId);
  }

}



function pause_Click(){
  console.log("pauseClick");
}

function showCompletedTasksToggle(){
  console.log("showCompletedTasksToggle");
}

