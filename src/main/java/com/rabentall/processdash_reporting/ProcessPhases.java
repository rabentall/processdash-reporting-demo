package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class ProcessPhases extends DashData{

  List<ProcessPhase> processPhases = new ArrayList<ProcessPhase>();

  void load(PDashContext ctx) {
    
    String hql = "select distinct pi.phase.process.name, pi.phase.ordinal, pi.phase.shortName, pi.phase.typeName from PlanItem as pi order by 1,2";

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

        ProcessPhase p = new ProcessPhase();
        p.process = (String)row[0];
        p.ordinal = (Integer)row[1];
        p.shortName = (String)row[2];
        p.typeName = (String)row[3];
        
        processPhases.add(p); 
    }
    

  }
}

class ProcessPhase{
  Object process;
  Integer ordinal;
  Object shortName;
  Object typeName;
  ProcessPhase(){} 
}