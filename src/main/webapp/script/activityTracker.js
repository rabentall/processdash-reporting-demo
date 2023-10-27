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

































async function getTaskMap(){
  //Get list of tasks from task API + build lookup map:
  const taskLookup = new Map();    

  try{
    const response = await fetch("http://localhost:2468/api/v1/tasks/");
    const tasksJson = await response.json();

    tasksJson.tasks.forEach((task) => {
        taskLookup.set("/" + task.project.fullName + "/" + task.fullName, task.id);
    });
  } catch (error) {
    console.error("Error in getTaskMap:", error.message);
  }    

  return taskLookup;
}

async function initTaskListTable(taskList){

  const taskLookup = await getTaskMap();

  var timerTable = new DataTable('#timerTable', {
    columns: [
      { title: 'Key', visible: false },
      { title: 'PlanItem' },
      { title: 'PlanTimeMin' },
      { title: 'ActualTimeMin' }
    ],
    data: taskList,
    "initComplete": function(settings, json) {
      //Hide spinner when table load completed:
      document.getElementById("pageLoader").style.display = "none";
    }
  });

  timerTable.on('click', 'tbody tr', function () {

    

    let activeTaskPath = timerTable.row(this).data()[1];

    document.getElementById("currentTask").innerHTML = activeTaskPath;    
    
    activeTaskId = taskLookup.get(activeTaskPath); //Lookup from PlanItem path.

    console.log("activeTaskId:" + activeTaskId);

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