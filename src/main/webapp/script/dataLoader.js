const PROTOCOL = window.location.protocol;
const HOST     = window.location.host;
const PLUGIN   = "pdash-reporting-rbentall-1.0";
const API      = "api/v1";

const URL_BASE      = PROTOCOL + "//" + HOST;
const JSONVIEWS_URL = URL_BASE + "//" + PLUGIN + "/jsonViews";
const TIMER_URL     = URL_BASE + "/" + API + "/timer/";
const TASKS_URL     = URL_BASE + "/" + API + "/tasks/";

var directHoursRoot_;
var overheadHoursRoot_;
var offworkRook_;
var defaultOverheadTask_;
var defaultOffworkTask_;

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

const timerTableTasks_ = new Map();

class TaskDetails{
    constructor(task){
      this.planItem             = task.planItem;
      this.planTimeHours        = task.planTimeHours.toFixed(2);
      this.actualTimeHours      = task.actualTimeHours.toFixed(2);
      this.activityStatus       = task.activityStatus;
      this.planDate             = getNullableDateValue(task, 'planDate');
      this.replanDate           = getNullableDateValue(task, 'replanDate');
      this.forecastDate         = getNullableDateValue(task, 'forecastDate');
      this.actualStartDate      = getNullableDateValue(task, 'actualStartDate');
      this.actualCompletionDate = getNullableDateValue(task, 'actualCompletionDate');
      this.planItem             = getLabel(task.planItem);
    }
  }

  /**
   * Returns data for the tasklist, using the jsonviews API:
   */
  async function getTaskDetails(){

    taskDetails_.clear();

    try{
      const response = await fetch(JSONVIEWS_URL + "/tasks");
      const taskListJson = await response.json();

      taskListJson.tasks.forEach((task) => {
        taskDetails_.set(
          task.planItem,
          new TaskDetails(task)
          );

      });
      console.log("** taskDetailsSize:" + taskDetails_.size);
    } catch (error) {
      console.error("Error in getTaskDetails:", error.message);
    }
  }

  async function getTimerTableTasks(){

    timerTableTasks_.clear();

    try{
      const response = await fetch(TASKS_URL);
      const timerTasksJson = await response.json();

      timerTasksJson.tasks.forEach((task) => {

        var planItem = task.project.fullName + "/" + task.fullName;

        let noteText = getNote(planItem);
        let elipsis = (noteText != "") ? "..." : "";

        if(planItem.startsWith(directHoursRoot_) || planItem.startsWith(overheadHoursRoot_) || planItem.startsWith(offworkRook_) ){

          if(taskDetails_.has(planItem)){

            var td =  taskDetails_.get(planItem);

            timerTableTasks_.set(planItem, [
              task.id,
              planItem,
              elipsis,
              td.planTimeHours,
              td.actualTimeHours,
              td.activityStatus,
              td.planDate,
              td.replanDate,
              td.forecastDate,
              td.actualStartDate,
              td.actualCompletionDate,
              getLabel(planItem),
              noteText
            ]);
          } else{

            //TODO - INCLUDE ACTUAL EFFORT FOR TIMER TASKS?
            timerTableTasks_.set(planItem, [
              task.id,
              planItem,
              "",
              "",
              "",
              "OTHER",
              "",
              "",
              "",
              "",
              "",
              "",
              ""
            ]);
          }
        }else{
          console.log("** EXCLUDE:" + planItem);
        }
      });
    } catch (error) {
      console.error("Error in getTaskMap:", error.message);
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

  async function getLabels(){

    labels_.clear();

    try{
      const response = await fetch(JSONVIEWS_URL + "/customColumns");
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

    notes_.clear();

    try{
      const response = await fetch(JSONVIEWS_URL + "/notes");
      const notesJson = await response.json();

      notesJson.notes.forEach((note) => {

        notes_.set(note.planItem, note.note);

      });
    } catch (error) {
      console.error("Error in getLabels:", error.message);
    }
  }

  async function getDashboardSettings(){
    try{
      const response = await fetch(JSONVIEWS_URL + "/dashboardSettings");
      const json = await response.json();
      const settingsKeys = Object.keys(json.dashboardSettings);

      console.log("** DashboardSettingsKeys: " + settingsKeys);

      directHoursRoot_     = getSetting(json.dashboardSettings, "timer.directHoursRoot");
      overheadHoursRoot_   = getSetting(json.dashboardSettings, "timer.overheadHoursRoot");
      offworkRook_         = getSetting(json.dashboardSettings, "timer.offworkRoot");
      defaultOverheadTask_ = getSetting(json.dashboardSettings, "timer.defaultOverheadTask");
      defaultOffworkTask_  = getSetting(json.dashboardSettings, "timer.defaultOffworkTask");

    } catch (error) {
      console.error("Error in getDashboardSettings:", error.message);
    }
  }

  function getSetting(obj, key){
    if(obj.hasOwnProperty('timer.overheadHoursRoot')){
      const setting = obj[key];
      console.log(key + " = " + setting);
      return setting;
    }else{
      console.log("Missing setting " + key);
    }
  }