package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class SizeMetrics extends DashData{

  List<SizeMetric> sizeMetrics = new ArrayList<SizeMetric>();

  void load(PDashContext ctx) {
    
    String hql = 
        " select " +
        "     size.planItem.project.key, " +
        "     size.planItem.wbsElement.key, " +
        "     size.sizeMetric.shortName, " +
        "     size.measurementType.name, " +
        "     sum(size.addedAndModifiedSize) " +
        " from SizeFact as size " +
        " group by size.planItem.project.key, size.planItem.wbsElement.key, size.sizeMetric.shortName, size.measurementType.name ";


    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

        SizeMetric sm = new SizeMetric();
        sm.planItemProjectId             = (Integer)row[0];
        sm.planItemWbsElementId          = (Integer)row[1];        
        sm.sizeMetricShortName           = (String)row[2];
        sm.sizeMetricMeasurementTypeName = (String)row[3];
        sm.addedAndModifiedSize          = (Double)row[4];
        
        sizeMetrics.add(sm); 
    }
    

  }
}

class SizeMetric{
  Integer planItemProjectId;
  Integer planItemWbsElementId;
  String  sizeMetricShortName;
  String  sizeMetricMeasurementTypeName;
  Double  addedAndModifiedSize;
  
  SizeMetric(){} 
}