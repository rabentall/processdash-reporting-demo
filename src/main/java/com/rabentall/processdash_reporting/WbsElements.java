package com.rabentall.processdash_reporting;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class WbsElements extends DashData{

  List<WbsElement> wbsElements = new ArrayList<WbsElement>();

  void load(PDashContext ctx) {
    
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
    for (Object[] row : getRows(ctx, hql)) {

        WbsElement wbs = new WbsElement();
        wbs.projectId = (Integer)row[0];   
        wbs.wbsElementId = (Integer)row[1];           
        wbs.project = (String)row[2];
        wbs.wbsElement = (String)row[3]; 
        wbs.process = (String)row[4]; 
        wbs.planTimeHours = (Double)row[5];
        wbs.actualTimeHours = (Double)row[6];
        wbs.actualStartDate = (Date)row[7];
        wbs.actualCompletionDate = (Date)row[8];
        wbs.isComplete = ((Number)row[9] == (Number)row[10]);

        wbsElements.add(wbs); 
    }
  }
}

class WbsElement{
  Integer projectId;
  Integer wbsElementId;
  String  project;
  String  wbsElement;
  String  process;
  Double  planTimeHours;
  Double  actualTimeHours;
  Date    actualStartDate;
  Date    actualCompletionDate;
  Boolean isComplete;
  WbsElement(){} 
}