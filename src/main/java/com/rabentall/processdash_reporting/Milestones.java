package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import net.sourceforge.processdash.api.PDashContext;

class Milestones extends DashData{

  List<Milestone> milestones = new ArrayList<Milestone>();
  //TODO - make threadsafe
  //TODO - cache - refresh interval.
  void load(PDashContext ctx) {
    
    String hql =
    " select                                                         " +
    "   dep.predecessor.id,                                          " +
    "   dep.predecessor.project.id,                                  " +
    "   dep.predecessor.wbsElement.id,                               " +
    "   dep.predecessor.task.id,                                     " +        
    "   dep.successor.task.name,                                     " +
    "   piaf.value.text                                              " +
    " from PlanItemAttrFact as piaf, PlanItemDependencyFact as dep   " +
    " where                                                          " +
    " dep.successor.id = piaf.planItem.id and                        " +
    " dep.successor.phase.name = 'Milestone' and                     " +
    " piaf.planItem.phase.identifier = '*Unspecified*/Milestone' and " +
    " piaf.attribute.name='Milestone Commit Date'                    ";

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

        Milestone m = new Milestone();
        m.planItemId   = (Integer)row[0];
        m.projectId    = (Integer)row[1];
        m.wbsElementId = (Integer)row[2];
        m.taskId       = (Integer)row[3];                        
        m.name         = (String) row[4];

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            m.commitDate    = (sdf.parse((String)row[5]));
        }
        catch(ParseException ex){
            System.out.println("Error parsing milestone date:" + row[5]);
        }
        
        milestones.add(m);
    }
  }
}

class Milestone{

  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Integer taskId;
  String  name;
  Date    commitDate;
  
  Milestone(){} 
}