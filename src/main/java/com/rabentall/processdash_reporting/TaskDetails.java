package com.rabentall.processdash_reporting;

import net.sourceforge.processdash.api.PDashContext;

class TaskDetails extends DashDataMap{

  void load(PDashContext ctx) {

    String hql =
    " select       " +
    "    t.id,     " +
    "    t.name    " +
    " from         " +
    "    Task as t ";

    load(ctx, hql);
  }

  void addElement(Object[] row){
    int key = (Integer)row[0];
    if(elements.containsKey(key)){
      logger.severe(String.format("Warning - duplicate row encountered:%s", getRawRow(row)));
      ++duplicateRowCount;
    }else{
      elements.put((Integer)row[0], (String)row[1]);
    }
  }
}

class TaskDetail extends DashDataElement{
  Integer taskId;
  String  name;

  TaskDetail(Object[] row){
    taskId   = (Integer)row[0];
    name     = (String)row[1];
  }
  TaskDetail(){}
}