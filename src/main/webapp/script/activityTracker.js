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

  new DataTable('#timerTable', {
    columns: [
      { title: 'Key' },
      { title: 'PlanItem' },
      { title: 'PlanTimeMin' },
      { title: 'ActualTimeMin' },
      { 
        title: 'Timer',
        data:  null,
        render: function(data) {
            activeTaskId = taskLookup.get(data[0]);

            if(taskLookup.has(data[0])){
              data = 
              '<A HREF= "javascript:javascript:void(0)" onClick="javascript:toggleTimer(' + activeTaskId + ')">' + 
              '<img border="0" title="Start timing" src="/control/startTiming.png">' + 
              '</A>';
            }else{
              //FIXMEconsole.error("Key missing from taskLookup:" + data[0]);
            }

            return data;
        }
      }      
    ],
    data: taskList
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