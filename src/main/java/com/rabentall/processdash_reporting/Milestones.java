package com.rabentall.processdash_reporting;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import net.sourceforge.processdash.api.PDashContext;

class Milestones extends DashDataList{

  transient String MILESTONE_COLOR_HQL =
  " select                                 " +
  "   piaf.planItem.id,                    " +
  "   piaf.value.text                      " +
  " from                                   " +
  "   PlanItemAttrFact as piaf             " +
  " where                                  " +
  "   piaf.attribute.name='Milestone Color'";

  transient Lookup<String> milestoneColors_ = new Lookup<String>(MILESTONE_COLOR_HQL);

  transient String MILESTONE_COMMT_DATE_HQL =
  " select                                       " +
  "   piaf.planItem.id,                          " +
  "   piaf.value.text                            " +
  " from                                         " +
  "   PlanItemAttrFact as piaf                   " +
  " where                                        " +
  "   piaf.attribute.name='Milestone Commit Date'";

  transient Lookup<String> milestoneCommitDate_ = new Lookup<String>(MILESTONE_COMMT_DATE_HQL);


  void load(PDashContext ctx) {

    description = "Milestone information for each planItem.";

    milestoneColors_.load(ctx);
    milestoneCommitDate_.load(ctx);

    String hql =
    " select                                                         " +
    "   dep.predecessor.id,                                          " +
    "   dep.predecessor.project.id,                                  " +
    "   dep.predecessor.wbsElement.id,                               " +
    "   dep.predecessor.task.id,                                     " +
    "   dep.predecessor,                                             " +
    "   dep.successor.id,                                            " +
    "   dep.successor,                                               " +
    "   dep.successor.task.name                                      " +
    " from PlanItemDependencyFact as dep                             " +
    " where                                                          " +
    "   dep.successor.phase.name = 'Milestone' and                   " +
    "   dep.successor.phase.identifier = '*Unspecified*/Milestone'   ";

    load(ctx, hql);
  }

  void addElement(Object[] row) throws ParseException{
    elements.add(new Milestone(row));
  }

  class Milestone extends DashDataElement{

    Integer planItemId;
    Integer projectId;
    Integer wbsElementId;
    Integer taskId;
    String  planItem;
    Integer milestonePlanItemId;
    String  milestonePlanItem;
    String  milestoneName;

    Date    commitDate;
    String  milestoneColor;

    Milestone(Object[] row) throws ParseException{
      planItemId          = (Integer)row[0];
      projectId           = (Integer)row[1];
      wbsElementId        = (Integer)row[2];
      taskId              = (Integer)row[3];
      planItem            = getNullablePlanItemString(row[4]);
      milestonePlanItemId = (Integer)row[5];
      milestonePlanItem   = getNullablePlanItemString(row[6]);
      milestoneName       = (String)row[7];

      milestoneColor      = milestoneColors_.elements.get(milestonePlanItemId);

      String milestoneCommitDateStr = milestoneCommitDate_.elements.get(milestonePlanItemId);

      //FIXME - UGLY. Also not sure the output is more useful than yyyy-mm-dd/
      //Dec 31, 2023 12:00:00 AM
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      commitDate    = sdf.parse(milestoneCommitDateStr);
    }
    Milestone(){}
  }
}