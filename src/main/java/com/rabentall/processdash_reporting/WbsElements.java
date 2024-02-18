package com.rabentall.processdash_reporting;
import java.util.Date;

import net.sourceforge.processdash.api.PDashContext;

class WbsElements extends DashDataList{

  void load(PDashContext ctx) {

    String hql =
    " select " +
    "   tsf.planItem.project.key, " +
    "   tsf.planItem.wbsElement.key, " +
    "   tsf.planItem.project.name, " +
    "   tsf.planItem.wbsElement.name, " +
    "   tsf.planItem.phase.process.name,   " +
    "   sum(tsf.planTimeMin/60.0), " +
    "   sum(tsf.actualTimeMin/60.0), " +
    "   min(tsf.actualStartDate), " +
    "   max(tsf.actualCompletionDate), " +
    "   count(*), " +
    "   sum(case when tsf.actualStartDateDim.key      =  99999830 then 0 else 1 end), " +
    "   sum(case when tsf.actualCompletionDateDim.key =  99999830 then 0 else 1 end) " +
    " from TaskStatusFact as tsf " +
    " group by  " +
    "   tsf.planItem.project.key, " +
    "   tsf.planItem.wbsElement.key, " +
    "   tsf.planItem.project.name, " +
    "   tsf.planItem.wbsElement.name, " +
    "   tsf.planItem.phase.process.name " +
    " order by " +
    "   tsf.planItem.project.key, " +
    "   tsf.planItem.wbsElement.key ";

    load(ctx, hql);
  }

  void addElement(Object[] row) {
    elements.add(new WbsElement(row));
  }

}

class WbsElement extends DashDataElement{
  Integer projectId;
  Integer wbsElementId;
  String  project;
  String  wbsElement;
  String  process;
  Double  planTimeHours;
  Double  actualTimeHours;
  Date    actualStartDate;
  Date    actualCompletionDate;
  Long    countOfTasks;
  Long    countOfStartedTasks;
  Long    countOfCompletedTasks;
  Boolean isComplete;
  ActivityStatus activityStatus;

  WbsElement(Object[] row){
    projectId              = (Integer)row[0];
    wbsElementId           = (Integer)row[1];
    project                = (String)row[2];
    wbsElement             = (String)row[3];
    process                = (String)row[4];
    planTimeHours          = (Double)row[5];
    actualTimeHours        = (Double)row[6];
    actualStartDate        = (Date)row[7];
    actualCompletionDate   = (Date)row[8];
    countOfTasks           = (Long)row[9];
    countOfStartedTasks    = (Long)row[10];
    countOfCompletedTasks  = (Long)row[11];

    if(countOfStartedTasks == 0){
      activityStatus = ActivityStatus.TODO;
    } else if(countOfStartedTasks > 0 && countOfCompletedTasks < countOfTasks){
      activityStatus = ActivityStatus.WIP;
    } else if(countOfCompletedTasks == countOfTasks){
      activityStatus = ActivityStatus.COMPLETED;
    } else {
      activityStatus = ActivityStatus.UNKNOWN;
    }
  }

  WbsElement(){}
}