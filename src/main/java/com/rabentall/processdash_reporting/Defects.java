package com.rabentall.processdash_reporting;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class Defects extends DashData{

  List<Defect> defects = new ArrayList<Defect>();

  void load(PDashContext ctx) {    
    String hql = 
        " select                                       " +
        "   d.key,                                     " +
        "   d.planItem.key,                            " +
        "   d.planItem.project.key,                    " +
        "   d.planItem.wbsElement.key,                 " +
        "   d.planItem.task.key,                       " +        
        "   d.foundDate,                               " +
        "   d.defectType.name,                         " +
        "   d.injectedPhase.process.name,              " +
        "   d.injectedPhase.ordinal,                   " +
        "   d.injectedPhase.shortName,                 " +
        "   d.removedPhase.process.name,               " +
        "   d.removedPhase.ordinal,                    " +
        "   d.removedPhase.shortName,                  " +
        "   d.fixTimeMin,                              " +
        "   d.fixCount,                                " +
        "   d.fixPending,                              " +
        "   d.description.text                         " +
        " from DefectLogFact as d order by d.foundDate ";

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {
        defects.add(new Defect(row)); 
    }   
  }
}

class Defect{
  Integer id;
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId; 
  Integer taskId;
  Date    foundDate;
  String  defectType;
  String  injectedPhaseProcess;
  Integer injectedPhaseOrdinal;
  String  injectedPhase;
  String  injected; 
  String  removedPhaseProcess;
  Integer removedPhaseOrdinal;
  String  removedPhase;
  String  removed;      
  Float   fixTimeMin;
  Short   fixCount;
  Boolean fixPending;  
  String  description;

  Defect(Object[] row){
    id                    = (Integer)row[0];
    planItemId            = (Integer)row[1];
    projectId             = (Integer)row[2];
    wbsElementId          = (Integer)row[3];
    taskId                = (Integer)row[4];
    foundDate             = (Date)   row[5];
    defectType            = (String) row[6];
    injectedPhaseProcess  = (String) row[7];
    injectedPhaseOrdinal  = (Integer)row[8];
    injectedPhase         = (String) row[9];
    injected              = String.format("%s.%02d.%s", injectedPhaseProcess, injectedPhaseOrdinal, injectedPhase);    
    removedPhaseProcess   = (String) row[10];
    removedPhaseOrdinal   = (Integer)row[11];
    removedPhase          = (String) row[12];
    removed               = String.format("%s.%02d.%s", removedPhaseProcess, removedPhaseOrdinal, removedPhase);
    fixTimeMin            = (Float)  row[13];
    fixCount              = (Short)  row[14];
    fixPending            = (Boolean)row[15];    
    description           = (String) row[16];
  }

  Defect(){} 
}