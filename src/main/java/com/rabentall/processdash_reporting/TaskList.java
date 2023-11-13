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
    "     tsf.planItem.key,                          " +
    "     tsf.planItem.project.name,                 " +
    "     tsf.planItem.wbsElement.name,              " +
    "     tsf.planItem.task.name,                    " +
    "     tsf.planItem.phase.name,                   " +
    "     tsf.planTimeMin/60.0,                      " +
    "     tsf.actualTimeMin/60.0,                    " +
    "     tsf.actualStartDate,                       " +
    "     tsf.actualCompletionDate,                  " +
    "     tdf.planItem.id,                           " +
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

      tsf.planItemKey = (Integer)row[0];
      tsf.planItemProject = (String)row[1];
      tsf.planItemWbsElement = (String)row[2]; 
      tsf.planItemTask = (String)row[3];  
      tsf.planItemPhase = (String)row[4];                      
      tsf.planTimeHours = (Double)row[5];
      tsf.actualTimeHours = (Double)row[6];
      tsf.actualStartDate = (Date)row[7];
      tsf.actualCompletionDate = (Date)row[8];
      tsf.planItem = tsf.planItemProject + "/" +tsf.planItemWbsElement + "/" + tsf.planItemTask;

      String measurementType = (String) row[10];

      switch(measurementType){
          case "Plan"     : tsf.planDate     = (Date)row[11]; break;
          case "Replan"   : tsf.replanDate   = (Date)row[11]; break;
          case "Forecast" : tsf.forecastDate = (Date)row[11]; break;

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
  Integer planItemKey;
  String  planItemProject;
  String  planItemWbsElement;
  String  planItemTask;
  String  planItemPhase;    
  Double  planTimeHours;
  Double  actualTimeHours;
  Date    actualStartDate;
  Date    actualCompletionDate;
  Date    planDate;
  Date    replanDate;
  Date    forecastDate;
  String  planItem;
  TaskListRow(){} 
}