package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

/**
 * \brief Returns the set of ProcessPhases defined within this dashboard.
 */
class ProcessPhases extends DashDataList{

  void load(PDashContext ctx) {

    //TODO - DONT USE PI!

    String hql =
        " select distinct " +
        "   pi.phase.process.name, " +
        "   pi.phase.ordinal,      " +
        "   pi.phase.shortName,    " +
        "   pi.phase.typeName      " +
        " from PlanItem as pi order by 1,2";

    load(ctx, hql);

  }

  void addElement(Object[] row){
    elements.add(new ProcessPhase(row));
  }

  class ProcessPhase extends DashDataElement{
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
}