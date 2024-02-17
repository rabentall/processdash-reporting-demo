package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

/*
 * Add in dep.predecessor and dep.successor
 *
 * second query to get successor phase identifier.
 *
 */

class Dependencies extends DashDataList{

  void load(PDashContext ctx) {

    description = "List of dependencies...";

    String hql =
      " select                " +
      "   dep.key,            " +
      "   dep.type.name,      " +
      "   dep.predecessor.id, " +
      "   dep.successor.id    " +
      " from PlanItemDependencyFact as dep";

      load(ctx, hql);
  }

  DashDataElement create(Object[] row){
    return new Dependency(row);
  }
}

class Dependency implements DashDataElement{
  Integer Id;
  String  type;
  Integer predecessorId;
  Integer successorId;

  Dependency(Object[] row){
    Id            = (Integer)row[0];
    type          = (String) row[1];
    predecessorId = (Integer)row[2];
    successorId   = (Integer)row[3];
  }
  Dependency(){}
}