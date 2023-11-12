package com.rabentall.processdash_reporting;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class TimeLog extends DashData{

  List<TimeLogRow> timeLog = new ArrayList<TimeLogRow>();
  
  TimeLog(PDashContext ctx){
    super(ctx);
  } 

  void load() {
    
    String hql = 
    " select                         " + 
    "   tlf.planItem.key,            " + 
    "   tlf.planItem.project.key,    " + 
    "   tlf.planItem.wbsElement.key, " + 
    "   tlf.deltaMin,                " +
    "   tlf.interruptMin,            " +
    "   tlf.startDate,               " +
    "   tlf.endDate                  " +
    " from                           " +
    "   TimeLogFact as tlf           ";

    // iterate over the data we received from the database
    for (Object[] row : getRows(hql)) {

        TimeLogRow tlr = new TimeLogRow();
        tlr.planItemId                   = (Integer)row[0];
        tlr.planItemProjectId            = (Integer)row[1];
        tlr.planItemWbsElementId         = (Integer)row[2];        
        tlr.deltaMin                     = (Float)row[3];
        tlr.interruptMin                 = (Float)row[4];
        tlr.startDate                    = (Date)row[5];
        tlr.endDate                      = (Date)row[6];        
        
        timeLog.add(tlr); 
    }
  }
}

class TimeLogRow{
  Integer planItemId;
  Integer planItemProjectId;
  Integer planItemWbsElementId;
  Float   deltaMin;
  Float   interruptMin;
  Date    startDate;
  Date    endDate;
  
  TimeLogRow(){} 
}