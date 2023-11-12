package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;

class Notes extends DashData{

  List<Note> notes = new ArrayList<Note>();

  void load(PDashContext ctx) {

    String hql = "select pinf.planItem.key, pinf.note.text from PlanItemNoteFact as pinf";

    // iterate over the data we received from the database
    for (Object[] row : getRows(ctx, hql)) {

        Note n = new Note();
        n.planItemKey = (Integer)row[0];
        n.note = (String)row[1];
        
        notes.add(n); 
    }
    

  }
}

class Note{
  Integer planItemKey;
  String note;
  Note(){} 
}