package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

/**
 * \brief Returns notes associated with PlanItems.
 */
class Notes extends DashDataList{

  void load(PDashContext ctx) {

    String hql =
      " select                           " +
      "   pinf.id,                       " +
      "   pinf.planItem.id,              " +
      "   pinf.planItem.project.id,      " +
      "   pinf.planItem.wbsElement.id,   " +
      "   pinf.planItem.task.id,         " +
      "   pinf.note.text,                " +
      "   pinf.planItem,                 " +
      "   pinf.planItem.leafComponent,   " +
      "   pinf.planItem.leafTask         " +
      " from PlanItemNoteFact as pinf    ";

    load(ctx, hql);
  }

  void addElement(Object[] row) {
    elements.add(new Note(row));
  }

  class Note extends DashDataElement{
    Integer planItemId;
    Integer projectId;
    Integer wbsElementId;
    Integer taskId;
    String  note;
    String  planItem;
    Boolean isLeafComponent;
    Boolean isLeafTask;

    Note(Object[] row){
      id              = (Integer)row[0];
      planItemId      = (Integer)row[1];
      projectId       = (Integer)row[2];
      wbsElementId    = (Integer)row[3];
      taskId          = (Integer)row[4];
      note            = (String) row[5];
      planItem        = getNullablePlanItemString(row[6]);
      isLeafComponent = (Boolean)row[7];
      isLeafTask      = (Boolean)row[8];
    }
    Note(){}
  }
}