package com.rabentall.processdash_reporting;

import java.util.Map;
import java.util.HashMap;

import net.sourceforge.processdash.api.PDashContext;

class CustomColumns extends DashData{

  Map<String, CustomColumn> customColumns = new HashMap<String, CustomColumn>();
  
  CustomColumns(PDashContext ctx){
    super(ctx);
  } 

  void load() {
    
    //Ordered by attribute name:
    String hql =
    " select                       " +
    "    piaf.attribute.name,      " +    
    "    piaf.planItem.id,         " +
    "    piaf.value.text           " +
    " from                         " +
    "    PlanItemAttrFact as piaf  " +
    " order by                     " +
    "    piaf.attribute            ";

    String previousAttribute = "";

    CustomColumn customColumn = null;

    // iterate over the data we received from the database
    for (Object[] row : getRows(hql)) {

        String currentAttribute =  (String)row[0];

        //New custom column
        if(!currentAttribute.equals(previousAttribute)){
            
            if(customColumn != null){
              customColumns.put(previousAttribute, customColumn);
            }
            customColumn = new CustomColumn();
            customColumn.columnName = currentAttribute;

            previousAttribute = currentAttribute;
        }

        //populate:
        customColumn.customColumn.put(
          (Integer)row[1],
          (String)row[2]
          );
    }
  }
}

class CustomColumn{

  String columnName;
  Map<Integer, String> customColumn = new HashMap<Integer, String>();

  CustomColumn(){}

}