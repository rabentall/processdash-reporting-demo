package com.rabentall.processdash_reporting;
import java.util.List;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;
import net.sourceforge.processdash.api.PDashQuery;
import java.util.logging.Logger;

abstract class DashData {

    protected static final Logger logger = Logger.getLogger(DashData.class.getName());

    protected int goodRowCount = 0;
    protected int badRowCount  = 0;
    protected String description;
    protected List<DashDataElement> elements = new ArrayList<DashDataElement>();

    protected List<Object[]> getRows(PDashContext ctx, String hql){

      List<Object[]> rows = ctx.getQuery().query(hql, PDashQuery.FilterMode.CURRENT);

      logger.info("\n====== HQL query: ======\n" + hql.trim().replaceAll("\\s+", " ") + "\n========================");
      logger.info("Total rows returned:" + rows.size());

     return rows;
 }

    protected void load(PDashContext ctx, String hql){

        for (Object[] row : getRows(ctx, hql)) {
          addElement(row);
        }

        logger.info("GoodRowCount: " + goodRowCount);
        logger.info("BadRowCount:  " + badRowCount);
    }

    private void addElement(Object[] row){
      try{
        elements.add(create(row));
        ++goodRowCount;
      } catch(Exception ex){
        logger.severe(String.format("Bad row encountered:%s", getRawRow(row)));
        ++badRowCount;
      }
    }

    private String getRawRow(Object[] row){

      String delim = "";

      StringBuilder sb = new StringBuilder("[");

      for (Object o : row){
        sb.append(delim);
        sb.append(o);
        delim = "|";
      }
      sb.append("]");

      return sb.toString();
    }

    abstract DashDataElement create(Object[] row) throws Exception;

    //TODO - DELETE ME.
    abstract void load(PDashContext ctx);

}

enum ActivityStatus{
    TODO,
    WIP,
    COMPLETED,
    UNKNOWN
};
