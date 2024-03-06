package com.rabentall.processdash_reporting;

/**
 * \brief Base class for all data elements returned by the API.
 */
public abstract class DashDataElement{

    /**
     * \brief Unique identifier for this element.
     */
    Integer id;

    /**
     * \brief Produces planItem toString with only single "/" at start (so consistent with jsp behaviour).
     */
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
