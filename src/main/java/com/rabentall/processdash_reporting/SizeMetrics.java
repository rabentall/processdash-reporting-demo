package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

class SizeMetrics extends DashDataList{

  void load(PDashContext ctx) {

    String hql =
        " select " +
        "   size.planItem.project.key,        " +
        "   size.planItem.wbsElement.key,     " +
        "   size.sizeMetric.shortName,        " +
        "   size.measurementType.name,        " +
        "   sum(size.addedAndModifiedSize)    " +
        " from SizeFact as size               " +
        " group by size.planItem.project.key, " +
        "   size.planItem.wbsElement.key,     " +
        "   size.sizeMetric.shortName,        " +
        "   size.measurementType.name         ";

    load(ctx, hql);
  }

  DashDataElement create(Object[] row){
    return new SizeMetric(row);
  }

}

class SizeMetric implements DashDataElement{
  Integer projectId;
  Integer wbsElementId;
  String  shortName;
  String  measurementType;
  Double  addedAndModifiedSize;

  SizeMetric(Object[] row){
    projectId             = (Integer)row[0];
    wbsElementId          = (Integer)row[1];
    shortName             = (String)row[2];
    measurementType       = (String)row[3];
    addedAndModifiedSize  = (Double)row[4];
  }

  SizeMetric(){}
}