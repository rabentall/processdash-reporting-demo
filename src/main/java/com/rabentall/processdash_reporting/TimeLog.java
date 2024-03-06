package com.rabentall.processdash_reporting;

import java.util.Date;
import net.sourceforge.processdash.api.PDashContext;

/**
 * \brief Returns timelog data associated with this dashboard.
 */
class TimeLog extends DashDataList{

  void load(PDashContext ctx) {

    String hql =
    " select                         " +
    "   tlf.id,                      " +
    "   tlf.planItem.key,            " +
    "   tlf.planItem.project.key,    " +
    "   tlf.planItem.wbsElement.key, " +
    "   tlf.deltaMin,                " +
    "   tlf.interruptMin,            " +
    "   tlf.startDate,               " +
    "   tlf.endDate,                 " +
    "   tlf.planItem                 " +
    " from                           " +
    "   TimeLogFact as tlf           ";

    load(ctx, hql);
  }

  void addElement(Object[] row){
    elements.add(new TimeLogRow(row));
  }

  class TimeLogRow extends DashDataElement{
    Integer planItemId;
    Integer projectId;
    Integer wbsElementId;
    Float   deltaMin;
    Float   interruptMin;
    Date    startDate;
    Date    endDate;
    String  planItem;

    TimeLogRow(Object[] row){
      id                = (Integer)row[0];
      planItemId        = (Integer)row[1];
      projectId         = (Integer)row[2];
      wbsElementId      = (Integer)row[3];
      deltaMin          = (Float)row[4];
      interruptMin      = (Float)row[5];
      startDate         = (Date)row[6];
      endDate           = (Date)row[7];
      planItem          = getNullablePlanItemString(row[8]);
    }
    TimeLogRow(){}
  }
}