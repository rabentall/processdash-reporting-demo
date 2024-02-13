package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

class Notes extends DashData{

  void load(PDashContext ctx) {

    String hql =
      " select                           " +
      "   pinf.planItem.id,              " +
      "   pinf.planItem.project.id,      " +
      "   pinf.planItem.wbsElement.id,   " +
      "   pinf.planItem.task.id,         " +
      "   pinf.note.text,                " +
      "   pinf.planItem.project.name,    " +
      "   pinf.planItem.wbsElement.name, " +
      "   pinf.planItem.task.name        " +
      " from PlanItemNoteFact as pinf    ";

    load(ctx, hql);
  }

  DashDataElement create(Object[] row){
    return new Note(row);
  }
}

class Note implements DashDataElement{
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Integer taskId;
  Boolean isWbsElement;
  String  note;
  String  planItem;

  Note(Object[] row){
    planItemId   = (Integer)row[0];
    projectId    = (Integer)row[1];
    wbsElementId = (Integer)row[2];
    taskId       = (Integer)row[3];
    note         = (String)row[4];
    isWbsElement = (taskId == null);
    planItem     = (String) row[5] + "/" + (String) row[6] + "/" + (String) row[7];
  }
  Note(){}
}