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

    //lookup_.load(ctx);

    String hql =
    " select                          " +
    "    piaf.attribute.name,         " +
    "    piaf.planItem.id,            " +
    "    piaf.planItem.project.id,    " +
    "    piaf.planItem.wbsElement.id, " +
    "    piaf.planItem.task.id,       " +
    "    piaf.value.text,             " +
    "    piaf.planItem.project.name,    " +
    "    piaf.planItem.wbsElement.name " +
    " from                            " +
    "    PlanItemAttrFact as piaf     ";

    load(ctx, hql);
  }


  DashDataElement create(Object[] row){

    //Integer taskId = (Integer)row[4];
    //String  taskName = lookup_.get(taskId);

    return new CustomColumnRow(row, "FIXME");
  }

}

class CustomColumnRow implements DashDataElement{

  String  name;
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Integer taskId;
  String  value;
  Boolean isWbsElement;
  String  planItem;

  CustomColumnRow(Object[] row, String taskName){
    name         = (String)row[0];
    planItemId   = (Integer)row[1];
    projectId    = (Integer)row[2];
    wbsElementId = (Integer)row[3];
    taskId       = (Integer)row[4];
    value        = (String)row[5];
    isWbsElement = (taskId == null);
    planItem     = (String)row[6] + "/" + (String)row[7] + (taskName == null? "" : "/" + taskName) ;
  }

  CustomColumnRow(){}
}