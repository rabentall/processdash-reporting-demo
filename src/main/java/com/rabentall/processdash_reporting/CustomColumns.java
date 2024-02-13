package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class CustomColumns extends DashData{

  List<CustomColumnRow> customColumns = new ArrayList<CustomColumnRow>();

  void load(PDashContext ctx) {

    description = "Custom column values.";

    String hql =
    " select                          " +
    "    piaf.attribute.name,         " +
    "    piaf.planItem.id,            " +
    "    piaf.planItem.project.id,    " +
    "    piaf.planItem.wbsElement.id, " +
    "    piaf.planItem.task.id,       " +
    "    piaf.value.text,             " +
    "    piaf.planItem.project.name,    " +
    "    piaf.planItem.wbsElement.name, " +
    "    piaf.planItem.task.name        " +
    " from                            " +
    "    PlanItemAttrFact as piaf     ";

    load(ctx, hql);
  }


  DashDataElement create(Object[] row){
    return new CustomColumnRow(row);
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

  CustomColumnRow(Object[] row){
    name         = (String)row[0];
    planItemId   = (Integer)row[1];
    projectId    = (Integer)row[2];
    wbsElementId = (Integer)row[3];
    taskId       = (Integer)row[4];
    value        = (String)row[5];
    isWbsElement = (taskId == null);
    planItem     = (String)row[6] + "/" + (String)row[7] + "/" + (String)row[8];
  }

  CustomColumnRow(){}
}