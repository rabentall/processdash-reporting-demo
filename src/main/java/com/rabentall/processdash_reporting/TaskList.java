package com.rabentall.processdash_reporting;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class TaskList extends DashData{

  List<TaskListRow> taskList = new ArrayList<TaskListRow>();
  
  TaskList(PDashContext ctx){
    super(ctx);
  } 

  void load() {
    
    String hql =
    " select tsf.planItem.key, " +
    " tsf.planItem.project.name, " +
    " tsf.planItem.wbsElement.name, " +
    " tsf.planItem.task.name, " +
    " tsf.planItem.phase.name, " +    
    " tsf.planTimeMin/60.0, " +
    " tsf.actualTimeMin/60.0, " +
    " tsf.actualStartDate, " +
    " tsf.actualCompletionDate " +
    " from TaskStatusFact as tsf where tsf.actualCompletionDate = null order by tsf.planItem.key";

    // iterate over the data we received from the database
    for (Object[] row : getRows(hql)) {

        TaskListRow tsf = new TaskListRow();
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
  String  planItem;
  TaskListRow(){} 
}