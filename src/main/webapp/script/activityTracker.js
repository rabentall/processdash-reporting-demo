//
// TODO - cols reorder
// TODO - current task
// TODO - OH/DT
// TODO - Other times needed
// TODO - script lookups.
// TODO - frames layout
// TODO - server-side json to replace jstl.
// TODO - review code and comment up.
// TODO - stylesheets.
// TODO - Task ordering.
// TODO - variance
// TODO - WIP

//
// Populates map of task IDs + task paths in personal dashboard
//
async function getTaskMap(){
  //Get list of tasks from task API + build lookup map:
  const taskLookup = new Map();    

  try{
    const response = await fetch("http://localhost:2468/api/v1/tasks/");
    const tasksJson = await response.json();

    tasksJson.tasks.forEach((task) => {
        taskLookup.set("/" + task.project.name + "/" + task.fullName, task.id);
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
      { title: 'PlanItem' },
      { title: 'PlanTimeMin' },
      { title: 'ActualTimeMin' },
      { 
        title: 'Timer',
        data:  null,
        render: function(data) {
            activeTaskId = taskLookup.get(data[0]);
            data = 
              '<A HREF= "javascript:javascript:void(0)" onClick="javascript:toggleTimer(' + activeTaskId + ')">' + 
              '<img border="0" title="Start timing" src="/control/startTiming.png">' + 
              '</A>';

            return data;
        }
      }      
    ],
    data: taskList
  });  
}

//TODO - REORDER COLS

//
// TODO - DRAW TABLE:
// https://datatables.net/examples/data_sources/js_array
//

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