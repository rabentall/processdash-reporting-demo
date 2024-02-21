package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

class PlanItems extends DashDataList{

  transient Lookups lookups_;

  PlanItems(Lookups lookups){
    lookups_ = lookups;
  }

  void load(PDashContext ctx) {

    description = "Produces the PlanItem hierarchy as a flat list.";

    String hql =
      " select                         " +
      "   pi.id,                       " +
      "   pi.project.id,               " +
      "   pi.wbsElement.id,            " +
      "   pi.task.id,                  " +
      "   pi.project.name,             " +
      "   pi.wbsElement.name,          " +
      "   pi,                          " +
      "   pi.ordinal,                  " +
      "   pi.leafComponent,            " +
      "   pi.leafTask,                 " +
      "   pi.wbsElement.nameLength     " +
      " from                           " +
      "   PlanItem as pi               ";

      load(ctx, hql);
  }

  void addElement(Object[] row){
    elements.add(new PlanItem(row));
  }

  class PlanItem extends DashDataElement{

    Integer projectId;
    Integer wbsElementId;
    Integer taskId;
    String  projectName;
    String  wbsElementName;
    String  planItem;
    Short   ordinal;
    Boolean isLeafComponent;
    Boolean isLeafTask;
    Byte    wbsElementNameLength;

    String wbsElement_0;
    String wbsElement_1;
    String wbsElement_2;
    String wbsElement_3;
    String wbsElement_4;
    String wbsElement_5;

    String taskName;
    String processName;
    String phaseTypeName;
    String phaseShortName;

    PlanItem(Object[] row){
      id                   = (Integer)row[ 0];
      projectId            = (Integer)row[ 1];
      wbsElementId         = (Integer)row[ 2];
      taskId               = (Integer)row[ 3];
      projectName          = (String) row[ 4];
      wbsElementName       = (String) row[ 5];
      planItem             = getNullablePlanItemString(row[ 6]);
      ordinal              = (Short)  row[ 7];
      isLeafComponent      = (Boolean)row[ 8];
      isLeafTask           = (Boolean)row[ 9];
      wbsElementNameLength = (Byte)   row[10];

      String[] elems = wbsElementName.split("/");

      wbsElement_0 = getNamePart(elems, 0);
      wbsElement_1 = getNamePart(elems, 1);
      wbsElement_2 = getNamePart(elems, 2);
      wbsElement_3 = getNamePart(elems, 3);
      wbsElement_4 = getNamePart(elems, 4);
      wbsElement_5 = getNamePart(elems, 5);

      taskName       = lookups_.taskNames.get(id);
      processName    = lookups_.processNames.get(id);
      phaseTypeName  = lookups_.phaseTypeNames.get(id);
      phaseShortName = lookups_.phaseShortNames.get(id);

    }
    PlanItem(){}

    private String getNamePart(String[] elems, int part){
        if(part < elems.length){
            return elems[part];
        }else{
            return null;
        }
    }
  }
}
