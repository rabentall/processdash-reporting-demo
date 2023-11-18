package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class Notes extends DashData{

  List<Note> notes = new ArrayList<Note>();

  void load(PDashContext ctx) {

    String hql = "select pinf.planItem.id, pinf.planItem.project.id, pinf.planItem.wbsElement.id, pinf.planItem.task.id, pinf.note.text from PlanItemNoteFact as pinf";

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

        Note n = new Note();
        n.planItemId   = (Integer)row[0];
        n.projectId    = (Integer)row[1];
        n.wbsElementId = (Integer)row[2];
        n.taskId       = (Integer)row[3];                        
        n.note         = (String)row[4];
        
        n.isWbsElement = (n.taskId == null);

        notes.add(n); 
    }
    

  }
}

class Note{
  Integer planItemId;
  Integer projectId;
  Integer wbsElementId;
  Integer taskId;
  Boolean isWbsElement;
  String note;
  
  Note(){} 
}