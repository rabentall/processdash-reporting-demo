package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

/*
 * Add in dep.predecessor and dep.successor
 *
 * second query to get successor phase identifier.
 *
 */

class Dependencies extends DashDataList{

  transient String PHASE_IDENTIFIER_HQL =
  " select distinct" +
  "   dep.successor.id,             " +
  "   dep.successor.phase.name      " +
  " from                            " +
  "   PlanItemDependencyFact as dep ";

  transient Lookup<String> phaseIdentifiers_ = new Lookup<String>(PHASE_IDENTIFIER_HQL);

  void load(PDashContext ctx) {

    description = "List of dependencies...";

    phaseIdentifiers_.load(ctx);

    String hql =
      " select                         " +
      "   dep.key,                     " +
      "   dep.type.name,               " +
      "   dep.predecessor.id,          " +
      "   dep.predecessor,             " +
      "   dep.successor.id,            " +
      "   dep.successor                " +
      " from                           " +
      "   PlanItemDependencyFact as dep";

      load(ctx, hql);
  }

  void addElement(Object[] row){
    elements.add(new Dependency(row));
  }

  class Dependency extends DashDataElement{
    String  type;
    Integer predecessorId;
    String  predecessor;
    Integer successorId;
    String  successor;
    String  successorPhaseIdentifier;

    Dependency(Object[] row){
      id            = (Integer)row[0];
      type          = (String) row[1];
      predecessorId = (Integer)row[2];
      predecessor   = getNullablePlanItemString(row[3]);
      successorId   = (Integer)row[4];
      successor     = getNullablePlanItemString(row[5]);
      successorPhaseIdentifier = phaseIdentifiers_.elements.get(successorId);
    }
    Dependency(){}
  }

}
