package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class CustomColumns extends DashData{

  List<CustomColumnRow> customColumns = new ArrayList<CustomColumnRow>();

  void load(PDashContext ctx) {
    
    String hql =
    " select                       " +
    "    piaf.attribute.name,      " +
    "    piaf.planItem.id,         " +
    "    piaf.value.text           " +
    " from                         " +
    "    PlanItemAttrFact as piaf  "; 

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

        CustomColumnRow ccr = new CustomColumnRow();
        ccr.customColumnName = (String)row[0];
        ccr.planItemKey = (Integer)row[1];
        ccr.customColumnValue = (String)row[2];

        customColumns.add(ccr);
    }
  }
}

class CustomColumnRow{

  String  customColumnName;
  Integer planItemKey;
  String  customColumnValue;

  CustomColumnRow(){}
}