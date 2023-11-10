package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class Dependencies extends DashData{

  List<Dependency> dependencies = new ArrayList<Dependency>();
  
  Dependencies(PDashContext ctx){
    super(ctx);
  } 

  void load() {
    
    String hql = "select dep.key, dep.type.name, dep.predecessor.id, dep.successor.id from PlanItemDependencyFact as dep";

    // iterate over the data we received from the database
    for (Object[] row : getRows(hql)) {

        Dependency d = new Dependency();
        d.Id            = (Integer)row[0];
        d.type          = (String) row[1];
        d.predecessorId = (Integer)row[2];
        d.successorId   = (Integer)row[3];
        
        dependencies.add(d);
    }
  }
}

class Dependency{
  Integer Id;
  String  type;
  Integer predecessorId;
  Integer successorId;
  Dependency(){} 
}