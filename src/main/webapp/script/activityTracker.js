
//
// The array of recent tasks.
//
//let recentTasks_;

//
// Define maximum number of recent tasks to allow.
//
//let recentTasksCount = 25;

const taskLookup_ = new Map();

//
// Initialise array from timer
//
function onload(){
    console.log("** Page loaded");



    const tasks = 
        fetch("http://localhost:2468/api/v1/tasks/")
        .then(
            async (data) => {
                let tasksJson = await data.json();
                tasksJson.tasks.forEach((task) => {
                    taskLookup_.set("/" + task.project.name + "/" + task.fullName, task.id);
                });
            })
        .catch((error) => {
            console.error("**** Error caught in onload:", error);
        });
        return 
}

// function temp(param){

//     console.log("***" + param.project.name + "|" + param.fullName + ":" + param.id);
// }

//
// TODO - DRAW TABLE:
// https://datatables.net/examples/data_sources/js_array
//

function toggleTimer(planItem) {
    console.log("toggleTimer:" + planItem);
    console.log("lookup:" + taskLookup_.get(planItem));

    const requestOptions = {
        method: 'PUT',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body:  'activeTaskId=' + taskLookup_.get(planItem) + '&timing=true'
    };
  
    fetch('http://localhost:2468/api/v1/timer/', requestOptions)
        .then(response => response)
        .catch(error => {
            console.error('There was an error!', error);
        });
    console.log("got to end:" + planItem);
  }