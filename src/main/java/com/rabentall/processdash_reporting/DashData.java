package com.rabentall.processdash_reporting;
import java.util.List;
import java.text.ParseException;
import java.util.ArrayList;

import net.sourceforge.processdash.api.PDashContext;
import net.sourceforge.processdash.api.PDashQuery;
import java.util.logging.Logger;

abstract class DashData {

    protected static final Logger logger = Logger.getLogger(DashDataList.class.getName());  //specialised name?

    protected int goodRowCount       = 0;
    protected int badRowCount        = 0;
    protected int duplicateRowCount  = 0;

    protected String description;

    protected List<Object[]> getRows(PDashContext ctx, String hql){

      List<Object[]> rows = ctx.getQuery().query(hql, PDashQuery.FilterMode.CURRENT);

      logger.info("\n====== HQL query: ======\n" + hql.trim().replaceAll("\\s+", " ") + "\n========================");
      logger.info("Total rows returned:" + rows.size());

     return rows;
 }

    protected void load(PDashContext ctx, String hql){

        for (Object[] row : getRows(ctx, hql)) {
          try{
            addElement(row);
            ++goodRowCount;
          } catch(ParseException ex){
           logger.severe(String.format("Bad row encountered:%s", getRawRow(row)));
           ++badRowCount;
          }
        }
        logger.info("GoodRowCount: " + goodRowCount);
        logger.info("BadRowCount:  " + badRowCount);
        logger.info("DuplicateRowCount:  " + duplicateRowCount);
    }


    protected String getRawRow(Object[] row){

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

    abstract void addElement(Object[] row) throws ParseException;

    abstract void load(PDashContext ctx);

}
