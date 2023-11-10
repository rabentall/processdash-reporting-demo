package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import net.sourceforge.processdash.api.PDashContext;

class Milestones extends DashData{

  List<Milestone> milestones = new ArrayList<Milestone>();
  
  Milestones(PDashContext ctx){
    super(ctx);
  } 

  void load() {
    
    String hql =
    " select                                                         " +
    "   dep.predecessor.id,                                          " +
    "   dep.successor.task.name,                                     " +
    "   piaf.value.text                                              " +
    " from PlanItemAttrFact as piaf, PlanItemDependencyFact as dep   " +
    " where                                                          " +
    " dep.successor.id = piaf.planItem.id and                        " +
    " dep.successor.phase.name = 'Milestone' and                     " +
    " piaf.planItem.phase.identifier = '*Unspecified*/Milestone' and " +
    " piaf.attribute.name='Milestone Commit Date'                    ";

    // iterate over the data we received from the database
    for (Object[] row : getRows(hql)) {

        Milestone m = new Milestone();
        m.planItemId    = (Integer)row[0];
        m.milestoneName = (String) row[1];

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            m.commitDate    = (sdf.parse((String)row[2]));
        }
        catch(ParseException ex){
            System.out.println("Error parsing milestone date:" + row[2]);
        }
        
        milestones.add(m);
    }
  }
}

class Milestone{
  Integer planItemId;
  String  milestoneName;
  Date    commitDate;
  
  Milestone(){} 
}