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
  Integer key;
  Integer planItemKey;
  Integer planItemProjectKey;
  Integer planItemWbsElementKey; 
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
    key                   = (Integer)row[0];
    planItemKey           = (Integer)row[1];
    planItemProjectKey    = (Integer)row[2];
    planItemWbsElementKey = (Integer)row[3];
    foundDate             = (Date)   row[4];
    defectType            = (String) row[5];
    injectedPhaseProcess  = (String) row[6];
    injectedPhaseOrdinal  = (Integer)row[7];
    injectedPhase         = (String) row[8];
    injected              = String.format("%s.%02d.%s", injectedPhaseProcess, injectedPhaseOrdinal, injectedPhase);    
    removedPhaseProcess   = (String) row[9];
    removedPhaseOrdinal   = (Integer)row[10];
    removedPhase          = (String) row[11];
    removed               = String.format("%s.%02d.%s", removedPhaseProcess, removedPhaseOrdinal, removedPhase);
    fixTimeMin            = (Float)  row[12];
    fixCount              = (Short)  row[13];
    fixPending            = (Boolean)row[14];    
    description           = (String) row[15];
  }

  Defect(){} 
}