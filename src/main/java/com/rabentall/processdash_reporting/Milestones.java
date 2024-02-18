package com.rabentall.processdash_reporting;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import net.sourceforge.processdash.api.PDashContext;

class Milestones extends DashDataList{

  void load(PDashContext ctx) {

    String hql =
    " select                                                         " +
    "   dep.predecessor.id,                                          " +
    "   dep.predecessor.project.id,                                  " +
    "   dep.predecessor.wbsElement.id,                               " +
    "   dep.predecessor.task.id,                                     " +
    "   dep.successor.task.name,                                     " +
    "   piaf.value.text,                                              " +
    "   dep.predecessor.project.name,                   " +
    "   dep.predecessor.wbsElement.name,                " +
    "   dep.predecessor.task.name                       " +
    " from PlanItemAttrFact as piaf, PlanItemDependencyFact as dep   " +
    " where                                                          " +
    " dep.successor.id = piaf.planItem.id and                        " +
    " dep.successor.phase.name = 'Milestone' and                     " +
    " piaf.planItem.phase.identifier = '*Unspecified*/Milestone' and " +
    " piaf.attribute.name='Milestone Commit Date'                    ";

    load(ctx, hql);
  }

  void addElement(Object[] row) throws ParseException{
    elements.add(new Milestone(row));
  }
}

class Milestone extends DashDataElement{

  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Integer taskId;
  String  name;
  Date    commitDate;
  String  planItem;

  Milestone(Object[] row) throws ParseException{
    planItemId   = (Integer)row[0];
    projectId    = (Integer)row[1];
    wbsElementId = (Integer)row[2];
    taskId       = (Integer)row[3];
    name         = (String) row[4];
    planItem     = (String) row[6] + "/" + (String) row[7] + "/" + (String) row[8];

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    commitDate    = (sdf.parse((String)row[5]));
  }
  Milestone(){}
}