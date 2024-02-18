package com.rabentall.processdash_reporting;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

//TODO = HOW TO SIMPLIFY SO WE CAN HANDLE IN SAME WAY AS OTHERS????

import net.sourceforge.processdash.api.PDashContext;

class Tasks extends DashDataList{

  List<Task> tasks = new ArrayList<Task>();

  void load(PDashContext ctx) {

    String hql =
    " select                                         " +
    "     tsf.planItem.id,                           " +
    "     tsf.planItem.project.id,                   " +
    "     tsf.planItem.wbsElement.id,                " +
    "     tsf.planItem.task.id,                      " +
    "     tsf.planItem.project.name,                 " +
    "     tsf.planItem.wbsElement.name,              " +
    "     tsf.planItem.task.name,                    " +
    "     tsf.planItem.phase.name,                   " +
    "     tsf.planTimeMin/60.0,                      " +
    "     tsf.actualTimeMin/60.0,                    " +
    "     tsf.actualStartDate,                       " +
    "     tsf.actualCompletionDate,                  " +
    "     tdf.measurementType.name,                  " +
    "     tdf.taskDate.fullDate                      " +
    " from                                           " +
    "     TaskStatusFact as tsf, TaskDateFact as tdf " +
    " where                                          " +
    "     tdf.planItem = tsf.planItem                " +
    " order by                                       " +
    "     tsf.planItem, tdf.measurementType.name     ";

    Integer previousPlanItemId = null;
    Task task       = null;

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

      Integer planItemId = (Integer)row[0];

      if(!planItemId.equals(previousPlanItemId)){
        if(task != null){
            tasks.add(task);
        }
        task = new Task();
        previousPlanItemId = planItemId;
      }

      //TODO - RUNS ONCE FOR EACH DATE FIELD
      task.planItemId   = (Integer)row[0];
      task.projectId    = (Integer)row[1];
      task.wbsElementId = (Integer)row[2];
      task.taskId       = (Integer)row[3];

      task.project      = (String)row[4];
      task.wbsElement   = (String)row[5];
      task.task         = (String)row[6];
      task.phase        = (String)row[7];

      task.planTimeHours = (Double)row[8];
      task.actualTimeHours = (Double)row[9];
      task.actualStartDate = (Date)row[10];
      task.actualCompletionDate = (Date)row[11];

      task.planItem = task.project + "/" +task.wbsElement + "/" + task.task;

      task.isComplete = (task.actualCompletionDate != null);

      if(task.actualStartDate == null && task.actualCompletionDate == null){
        task.activityStatus = ActivityStatus.TODO;
      } else if(task.actualStartDate != null  && task.actualCompletionDate == null){
        task.activityStatus = ActivityStatus.WIP;
      } else if( task.actualCompletionDate != null){
        task.activityStatus = ActivityStatus.COMPLETED;
      } else {
        task.activityStatus = ActivityStatus.UNKNOWN;
      }

      String measurementType = (String) row[12];

      switch(measurementType){
          case "Plan"     : task.planDate     = (Date)row[13]; break;
          case "Replan"   : task.replanDate   = (Date)row[13]; break;
          case "Forecast" : task.forecastDate = (Date)row[13]; break;

          default: break;
      }
    }

    //Add last element - TODO - MORE ELEGANT SOLUTION
    if(task != null){
            tasks.add(task);
    }
  }

  void addElement(Object[] row) {
    //FIXME;
  }


}

class Task extends DashDataElement{
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Integer taskId;
  String  project;
  String  wbsElement;
  String  task;
  String  phase;
  Double  planTimeHours;
  Double  actualTimeHours;
  Date    actualStartDate;
  Date    actualCompletionDate;
  Date    planDate;
  Date    replanDate;
  Date    forecastDate;
  String  planItem;
  Boolean isComplete;
  ActivityStatus activityStatus;
  Task(){}
}