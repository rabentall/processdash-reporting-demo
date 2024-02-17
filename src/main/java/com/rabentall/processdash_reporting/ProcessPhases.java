package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

class ProcessPhases extends DashDataList{

  void load(PDashContext ctx) {

    String hql =
        " select distinct " +
        "   pi.phase.process.name, " +
        "   pi.phase.ordinal,      " +
        "   pi.phase.shortName,    " +
        "   pi.phase.typeName      " +
        " from PlanItem as pi order by 1,2";

    load(ctx, hql);

  }

  DashDataElement create(Object[] row){
    return new ProcessPhase(row);
  }

}

class ProcessPhase implements DashDataElement{
  Object process;
  Integer ordinal;
  Object shortName;
  Object typeName;

  ProcessPhase(Object[] row){
    process   = (String)row[0];
    ordinal   = (Integer)row[1];
    shortName = (String)row[2];
    typeName  = (String)row[3];
  }
  ProcessPhase(){}
}