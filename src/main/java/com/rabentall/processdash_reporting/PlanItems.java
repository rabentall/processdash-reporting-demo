package com.rabentall.processdash_reporting;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import net.sourceforge.processdash.api.PDashContext;

class PlanItems extends DashDataList{

  transient Lookups lookups_;

  PlanItems(Lookups lookups){
    lookups_ = lookups;
  }

  void load(PDashContext ctx) {

    description = "Produces the PlanItem hierarchy as a flat list.";

    String hql =
      " select                         " +
      "   pi.id,                       " +
      "   pi.project.id,               " +
      "   pi.wbsElement.id,            " +
      "   pi.task.id,                  " +
      "   pi.project.name,             " +
      "   pi.wbsElement.name,          " +
      "   pi,                          " +
      "   pi.ordinal,                  " +
      "   pi.leafComponent,            " +
      "   pi.leafTask,                 " +
      "   pi.wbsElement.nameLength     " +
      " from                           " +
      "   PlanItem as pi               " +
      " order by pi.id                 ";

      load(ctx, hql);
  }

  void addElement(Object[] row){
    elements.add(new PlanItem(row));
  }

  //State related to outline numbering. Relies on list being ordered by planItem.id
  //(ie the same ordering you get in the WBS editor).
  transient int           initialDepth_       = 0;
  transient boolean       isFirstElement_     = true;
  transient int           MAX_OUTLINE_LEVELS  = 20;
  transient List<Short>   outlineNumberArray_ = new ArrayList<Short>(Collections.nCopies(MAX_OUTLINE_LEVELS, (short)0));

  class PlanItem extends DashDataElement{

    Integer projectId;
    Integer wbsElementId;
    Integer taskId;
    String  projectName;
    String  wbsElementName;
    String  planItem;
    Short   ordinal;
    Boolean isLeafComponent;
    Boolean isLeafTask;
    Byte    wbsElementNameLength;

    String wbsElement_0;
    String wbsElement_1;
    String wbsElement_2;
    String wbsElement_3;
    String wbsElement_4;
    String wbsElement_5;

    String taskName;
    String processName;
    String phaseTypeName;
    String phaseShortName;

    String outlineNumber;
    String outlineHeading;
    int    outlineLevel;

    PlanItem(Object[] row){
      id                   = (Integer)row[ 0];
      projectId            = (Integer)row[ 1];
      wbsElementId         = (Integer)row[ 2];
      taskId               = (Integer)row[ 3];
      projectName          = (String) row[ 4];
      wbsElementName       = (String) row[ 5];
      planItem             = getNullablePlanItemString(row[ 6]);
      ordinal              = (Short)  row[ 7];
      isLeafComponent      = (Boolean)row[ 8];
      isLeafTask           = (Boolean)row[ 9];
      wbsElementNameLength = (Byte)   row[10];

      String[] elems = wbsElementName.split("/");

      wbsElement_0 = getNamePart(elems, 0);
      wbsElement_1 = getNamePart(elems, 1);
      wbsElement_2 = getNamePart(elems, 2);
      wbsElement_3 = getNamePart(elems, 3);
      wbsElement_4 = getNamePart(elems, 4);
      wbsElement_5 = getNamePart(elems, 5);

      taskName       = lookups_.taskNames.get(id);
      processName    = lookups_.processNames.get(id);
      phaseTypeName  = lookups_.phaseTypeNames.get(id);
      phaseShortName = lookups_.phaseShortNames.get(id);


      outlineNumber  = getOutlineNumber();
      outlineHeading = getOutlineHeading();
      outlineLevel   = getDepth() - initialDepth_ + 1;

    }
    PlanItem(){}

    private String getNamePart(String[] elems, int part){
        if(part < elems.length){
            return elems[part];
        }else{
            return null;
        }
    }

    private int getDepth(){
      return planItem.length() - planItem.replace("/","").length();
    }

    private String getOutlineNumber(){
      //Depth is the absolute number of levels as implied by the number of "/" characters:
      if(isFirstElement_){
        initialDepth_   = getDepth();
        isFirstElement_ = false;
      }

      //Level is the difference between the current depth and the initial depth (so accounting for the root node having a non-zero depth):
      int currentLevel = getDepth() - initialDepth_;

      //The ordinal returns the order at "this" level:
      outlineNumberArray_.set(currentLevel, ordinal);

      //To build the numbered list, return the first n elements of the list
      StringBuilder outlineNumber = new StringBuilder();

      String sep = "";
      for(int ix = 0; ix <= currentLevel; ++ix){
        outlineNumber.append(sep);
        outlineNumber.append(String.format("%03d", outlineNumberArray_.get(ix)));
        sep = ".";
      }

      return outlineNumber.toString();
    }

    private String getOutlineHeading(){

      String[] elems = planItem.split("/"); //First element will be empty.

      assert elems.length > 0 : "Element list must be longer than 0";

      return elems[elems.length - 1]; //return the last element in the array
    }
  }
}
