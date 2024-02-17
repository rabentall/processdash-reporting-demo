package com.rabentall.processdash_reporting;

import java.util.Date;
import net.sourceforge.processdash.api.PDashContext;

class TimeLog extends DashDataList{

  void load(PDashContext ctx) {

    String hql =
    " select                         " +
    "   tlf.planItem.key,            " +
    "   tlf.planItem.project.key,    " +
    "   tlf.planItem.wbsElement.key, " +
    "   tlf.deltaMin,                " +
    "   tlf.interruptMin,            " +
    "   tlf.startDate,               " +
    "   tlf.endDate,                  " +
    "   tlf.planItem.project.name,    " +
    "   tlf.planItem.wbsElement.name, " +
    "   tlf.planItem.task.name        " +
    " from                           " +
    "   TimeLogFact as tlf           ";

    load(ctx, hql);
  }

  DashDataElement create(Object[] row){
    return new TimeLogRow(row);
  }
}

class TimeLogRow implements DashDataElement{
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Float   deltaMin;
  Float   interruptMin;
  Date    startDate;
  Date    endDate;
  String  planItem;

  TimeLogRow(Object[] row){
    planItemId        = (Integer)row[0];
    projectId         = (Integer)row[1];
    wbsElementId      = (Integer)row[2];
    deltaMin          = (Float)row[3];
    interruptMin      = (Float)row[4];
    startDate         = (Date)row[5];
    endDate           = (Date)row[6];
    planItem          = (String) row[7] + "/" + (String) row[8] + "/" + (String) row[9];
  }
  TimeLogRow(){}
}