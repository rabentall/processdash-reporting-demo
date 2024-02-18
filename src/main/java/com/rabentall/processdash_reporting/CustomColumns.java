package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

class CustomColumns extends DashDataList{

/**
 * key, attribute name, planitem key, planitem, text. Taskname results in rows being lost....
 * just using planItem results in // at front of path.
 * Do we really need wbsElement, project name? Think only really keys and planItem.
 */

//  TaskDetails lookup_ = new TaskDetails();

  void load(PDashContext ctx) {

    description = "Custom column values.";

    //lookup_.load(ctx); - FIXME

    String hql =
    " select                           " +
    "    piaf.id,                      " +
    "    piaf.attribute.name,          " +
    "    piaf.planItem.id,             " +
    "    piaf.planItem.project.id,     " +
    "    piaf.planItem.wbsElement.id,  " +
    "    piaf.planItem.task.id,        " +
    "    piaf.value.text,              " +
    "    piaf.planItem.project.name,   " +
    "    piaf.planItem.wbsElement.name " +
    " from                             " +
    "    PlanItemAttrFact as piaf      ";

    load(ctx, hql);
  }

  void addElement(Object[] row){
    elements.add(new CustomColumnRow(row, "FIXME"));
  }
}

class CustomColumnRow extends DashDataElement{

  String  name;
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Integer taskId;
  String  value;
  Boolean isWbsElement;
  String  planItem;

  CustomColumnRow(Object[] row, String taskName){
    id           = (Integer)row[0];
    name         = (String)row[1];
    planItemId   = (Integer)row[2];
    projectId    = (Integer)row[3];
    wbsElementId = (Integer)row[4];
    taskId       = (Integer)row[5];
    value        = (String)row[6];
    isWbsElement = (taskId == null);
    planItem     = (String)row[7] + "/" + (String)row[8] + (taskName == null? "" : "/" + taskName) ;
  }

  CustomColumnRow(){}
}