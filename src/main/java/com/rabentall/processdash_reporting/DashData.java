package com.rabentall.processdash_reporting;
import java.util.List;
import java.text.ParseException;

import net.sourceforge.processdash.api.PDashContext;
import net.sourceforge.processdash.api.PDashQuery;
import java.util.logging.Logger;

/**
 * \brief Base class for all data returned from the API.
 */
abstract class DashData {

    protected static final Logger logger = Logger.getLogger(DashDataList.class.getName());  //specialised name?

    /**
     * \brief Returns the count of raw data rows that are produced by the hql query.
     */
    protected int rawRowCount        = 0;

    /**
     * \brief Returns the count of rows that are correctly converted and returned as typed data.
     */
    protected int goodRowCount       = 0;

    /**
     * \brief Returns the count of rows that cannot be correctly converted and returned as typed data.
     */
    protected int badRowCount        = 0;

    /**
     * \brief For subclasses of DashDataMap, returns the count of rows with duplicate keys.
     */
    protected int duplicateRowCount  = 0;

    /**
     * \brief Returns a brief description of the data returned by the API.
     */
    protected String description;

    /**
     * \brief Executes an HQL query against the underlying database.
     * @param ctx The Process Dashboard Context being execute against.
     * @param hql The HQL query to be executed.
     * @return The list of raw (untyped) rows produced by the query.
     */
    protected List<Object[]> getRows(PDashContext ctx, String hql){

      @SuppressWarnings("unchecked")
      List<Object[]> rows = ctx.getQuery().query(hql, PDashQuery.FilterMode.CURRENT);
      rawRowCount = rows.size();

      logger.info("\n====== HQL query: ======\n" + hql.trim().replaceAll("\\s+", " ") + "\n========================");

      return rows;
    }

    /**
     * \brief Use to load data into a typed collection from results of the HQL query.
     * @param ctx The Process Dashboard Context being execute against.
     * @param hql The HQL query to be executed.
     */
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

        logger.info("RawRowCount:       " + rawRowCount);
        logger.info("GoodRowCount:      " + goodRowCount);
        logger.info("BadRowCount:       " + badRowCount);
        logger.info("DuplicateRowCount: " + duplicateRowCount);
    }

    /**
     * \brief Returns a string representation of a raw data row returned by the HQL query.
     * @param row The raw data row to be represented.
     * @return A delimited string representation of the data.
     */
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

    /**
     * \brief Must be implemented by subclasses to handle adding a data row to a typed collection.
     * @param The raw data row to be represented.
     * @throws ParseException if the object cannot be converted to the required type.
     */
    abstract void addElement(Object[] row) throws ParseException;

    /**
     * \brief Entry point method. Must be implemented by subclasses to handle data loading from HQL query to a typed collection.
     * @param ctx
     */
    abstract void load(PDashContext ctx);

}