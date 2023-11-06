package com.rabentall.processdash_reporting;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class WbsElementList extends DashData{

  List<WbsElementRow> wbsElementList = new ArrayList<WbsElementRow>();
  
  WbsElementList(PDashContext ctx){
    super(ctx);
  } 

  void load() {
    
    String hql =
    " select " + 
    "   tsf.planItem.project.key, " + 
    "   tsf.planItem.wbsElement.key, " + 
    "   tsf.planItem.project.name, " + 
    "   tsf.planItem.wbsElement.name, " + 
    "   tsf.planItem.phase.process.name,   " + 
    "   sum(tsf.planTimeMin/60.0), " + 
    "   sum(tsf.actualTimeMin/60.0), " + 
    "   min(tsf.actualStartDate), " + 
    "   max(tsf.actualCompletionDate), " + 
    "   sum(case when tsf.actualCompletionDateDim.key =  99999830 then 0 else 1 end), " + 
    "   count(*) " + 
    " from TaskStatusFact as tsf " + 
    " group by  " + 
    "   tsf.planItem.project.key, " + 
    "   tsf.planItem.wbsElement.key, " + 
    "   tsf.planItem.project.name, " + 
    "   tsf.planItem.wbsElement.name, " + 
    "   tsf.planItem.phase.process.name " + 
    " order by " + 
    "   tsf.planItem.project.key, " + 
    "   tsf.planItem.wbsElement.key ";

    // iterate over the data we received from the database
    for (Object[] row : getRows(hql)) {

        WbsElementRow wbs = new WbsElementRow();
        wbs.planItemProjectKey = (Integer)row[0];   
        wbs.planItemWbsElementKey = (Integer)row[1];           
        wbs.planItemProject = (String)row[2];
        wbs.planItemWbsElement = (String)row[3]; 
        wbs.process = (String)row[4]; 
        wbs.planTimeHours = (Double)row[5];
        wbs.actualTimeHours = (Double)row[6];
        wbs.actualStartDate = (Date)row[7];
        wbs.actualCompletionDate = (Date)row[8];

        wbs.wbsElement = wbs.planItemProject + "/" +wbs.planItemWbsElement;

        wbs.isComplete = ((Number)row[9] == (Number)row[10]);

        wbsElementList.add(wbs); 
    }
  }
}

class WbsElementRow{
  Integer planItemProjectKey;
  Integer planItemWbsElementKey;
  String  planItemProject;
  String  planItemWbsElement;
  String  process;
  Double  planTimeHours;
  Double  actualTimeHours;
  Date    actualStartDate;
  Date    actualCompletionDate;
  String  wbsElement;
  Boolean isComplete;
  WbsElementRow(){} 
}