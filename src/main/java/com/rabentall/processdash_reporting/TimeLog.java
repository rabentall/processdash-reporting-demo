package com.rabentall.processdash_reporting;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class TimeLog extends DashData{

  List<TimeLogRow> timeLog = new ArrayList<TimeLogRow>();

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

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

        TimeLogRow tlr = new TimeLogRow();
        tlr.planItemId                   = (Integer)row[0];
        tlr.projectId                    = (Integer)row[1];
        tlr.wbsElementId                 = (Integer)row[2];        
        tlr.deltaMin                     = (Float)row[3];
        tlr.interruptMin                 = (Float)row[4];
        tlr.startDate                    = (Date)row[5];
        tlr.endDate                      = (Date)row[6];        
        
        tlr.planItem                     = (String) row[7] + "/" + (String) row[8] + "/" + (String) row[9];

        timeLog.add(tlr); 
    }
  }
}

class TimeLogRow{
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Float   deltaMin;
  Float   interruptMin;
  Date    startDate;
  Date    endDate;
  String  planItem;
  
  TimeLogRow(){} 
}