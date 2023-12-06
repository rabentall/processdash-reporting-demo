package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class CustomColumns extends DashData{

  List<CustomColumnRow> customColumns = new ArrayList<CustomColumnRow>();

  void load(PDashContext ctx) {
    
    String hql =
    " select                          " +
    "    piaf.attribute.name,         " +
    "    piaf.planItem.id,            " +
    "    piaf.planItem.project.id,    " +
    "    piaf.planItem.wbsElement.id, " +    
    "    piaf.planItem.task.id,       " +    
    "    piaf.value.text,             " +
    "    piaf.planItem.project.name,    " + 
    "    piaf.planItem.wbsElement.name, " +
    "    piaf.planItem.task.name        " +
    " from                            " +
    "    PlanItemAttrFact as piaf     "; 

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

        CustomColumnRow ccr = new CustomColumnRow();
        ccr.name         = (String)row[0];
        ccr.planItemId   = (Integer)row[1];
        ccr.projectId    = (Integer)row[2];
        ccr.wbsElementId = (Integer)row[3];
        ccr.taskId       = (Integer)row[4];
        ccr.value        = (String)row[5];
        ccr.isWbsElement = (ccr.taskId == null);
        ccr.planItem     = (String)row[6] + "/" + (String)row[7] + "/" + (String)row[8];

        customColumns.add(ccr);
    }
  }
}

class CustomColumnRow{

  String  name;
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Integer taskId;
  String  value;
  Boolean isWbsElement;
  String  planItem;

  CustomColumnRow(){}
}