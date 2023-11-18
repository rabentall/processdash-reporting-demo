package com.rabentall.processdash_reporting;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class TaskList extends DashData{

  List<TaskListRow> taskList = new ArrayList<TaskListRow>();

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
    TaskListRow tsf       = null;

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

      Integer planItemId = (Integer)row[0];

      if(!planItemId.equals(previousPlanItemId)){
        if(tsf != null){
            taskList.add(tsf);
        }
        tsf = new TaskListRow();
        previousPlanItemId = planItemId;
      }

      tsf.planItemId   = (Integer)row[0];
      tsf.projectId    = (Integer)row[1];
      tsf.wbsElementId = (Integer)row[2];
      tsf.taskId       = (Integer)row[3];            

      tsf.project      = (String)row[4];
      tsf.wbsElement   = (String)row[5]; 
      tsf.task         = (String)row[6];  
      tsf.phase        = (String)row[7];      

      tsf.planTimeHours = (Double)row[8];
      tsf.actualTimeHours = (Double)row[9];
      tsf.actualStartDate = (Date)row[10];
      tsf.actualCompletionDate = (Date)row[11];

      tsf.planItem = tsf.project + "/" +tsf.wbsElement + "/" + tsf.task;

      tsf.isComplete = (tsf.actualCompletionDate != null);

      String measurementType = (String) row[12];

      switch(measurementType){
          case "Plan"     : tsf.planDate     = (Date)row[13]; break;
          case "Replan"   : tsf.replanDate   = (Date)row[13]; break;
          case "Forecast" : tsf.forecastDate = (Date)row[13]; break;

          default: break;                      
      }      
    }

    //Add last element - TODO - MORE ELEGANT SOLUTION
    if(tsf != null){
            taskList.add(tsf);
    }
  }
}

class TaskListRow{
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
  TaskListRow(){} 
}