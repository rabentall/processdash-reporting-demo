package com.rabentall.processdash_reporting;


public abstract class DashDataElement{
    Integer id;

    //Produces planItem toString with only single "/" at start (so consistent with jsp behaviour)
    protected String getNullablePlanItemString(Object obj){

        if(obj == null){
          return null;
        } else{
          String planItemRaw = obj.toString();
          assert planItemRaw.startsWith("//") : "planItem is expected to start with '//'.";
          return planItemRaw.substring(1);
        }
      }

}
