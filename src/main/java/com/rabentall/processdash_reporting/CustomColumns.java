package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

class CustomColumns extends DashDataList{

/**
 * key, attribute name, planitem key, planitem, text. Taskname results in rows being lost....
 * just using planItem results in // at front of path.
 * Do we really need wbsElement, project name? Think only really keys and planItem.
 */

  transient TaskDetails taskDetails_ = new TaskDetails();

  void load(PDashContext ctx) {

    description = "Custom column values.";

    taskDetails_.load(ctx);

    String hql =
    " select                            " +
    "    piaf.id,                       " +
    "    piaf.planItem.id,              " +
    "    piaf.planItem.project.id,      " +
    "    piaf.planItem.wbsElement.id,   " +
    "    piaf.planItem.task.id,         " +
    "    piaf.planItem.project.name,    " +
    "    piaf.planItem.wbsElement.name, " +
    "    piaf.attribute.name,           " +
    "    piaf.value.text                " +
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
    String  projectName;
    String  wbsElementName;
    String  taskName;
    String  attributeName;
    String  value;
    Boolean isWbsElement;
    String  planItem;

    CustomColumnRow(Object[] row){
      id             = (Integer)row[0];
      planItemId     = (Integer)row[1];
      projectId      = (Integer)row[2];
      wbsElementId   = (Integer)row[3];
      taskId         = (Integer)row[4];
      projectName    = (String) row[5];
      wbsElementName = (String) row[6];
      taskName       = taskDetails_.elements.get(id);
      attributeName  = (String)row[7];
      value          = (String)row[8];
      isWbsElement   = (taskId == null);

      planItem     = projectName + "/" + wbsElementName + (taskName == null ? "" : "/" + taskName);
    }

    CustomColumnRow(){}
  }
}