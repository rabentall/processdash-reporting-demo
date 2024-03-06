package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

/**
 * \brief Returns a list of custom columns and values.
 */
class CustomColumns extends DashDataList{

  void load(PDashContext ctx) {

    description = "Custom column values.";

    String hql =
    " select                            " +
    "    piaf.id,                       " +
    "    piaf.planItem.id,              " +
    "    piaf.planItem.project.id,      " +
    "    piaf.planItem.wbsElement.id,   " +
    "    piaf.planItem.task.id,         " +
    "    piaf.planItem,                 " +
    "    piaf.attribute.name,           " +
    "    piaf.value.text,               " +
    "    piaf.planItem.leafComponent,   " +
    "    piaf.planItem.leafTask         " +
    " from                              " +
    "    PlanItemAttrFact as piaf       ";

    load(ctx, hql);
  }

  void addElement(Object[] row){
    elements.add(new CustomColumnRow(row));
  }

  class CustomColumnRow extends DashDataElement{

    Integer planItemId;
    Integer projectId;
    Integer wbsElementId;
    Integer taskId;
    String  planItem;
    String  attributeName;
    String  value;
    Boolean isLeafComponent;
    Boolean isLeafTask;

    CustomColumnRow(Object[] row){
      id              = (Integer)row[0];
      planItemId      = (Integer)row[1];
      projectId       = (Integer)row[2];
      wbsElementId    = (Integer)row[3];
      taskId          = (Integer)row[4];
      planItem        = getNullablePlanItemString(row[5]);
      attributeName   = (String)row[6];
      value           = (String)row[7];
      isLeafComponent = (Boolean)row[8];
      isLeafTask      = (Boolean)row[9];
    }

    CustomColumnRow(){}
  }
}