package com.rabentall.processdash_reporting;
import net.sourceforge.processdash.api.PDashContext;

/**
 * \brief Specialisation of DashData that allows an arbitrary key-value lookup to be queried.
 */
class Lookup<T> extends DashDataMap<T> {

    /**
     * \brief The HQL query to return the key-value pair.
     * Note that this must return a pair of elements (K, V).
     */
    String hql_;

    /**
     * \brief Constructor to create an instance of Lookup. The query that is run by the
     * lookup is immutable.
     */
    Lookup(String hql){
        hql_ = hql;
    }

    /**
     * \brief Implementation of addElement to add a key-value pair to the lookup. Duplicate rows
     * are flagged as warnings not errors.
     */
    @SuppressWarnings("unchecked")
    @Override
    void addElement(Object[] row){
      int key = (Integer)row[0];
      if(elements.containsKey(key)){
        logger.severe(String.format("Warning - duplicate row encountered:%s", getRawRow(row)));
        ++duplicateRowCount;
      }else{
        elements.put((Integer)row[0], (T)row[1]);
      }
    }

    @Override
    void load(PDashContext ctx) {
        load(ctx, hql_);
    }

    T get(Integer key){
      return elements.get(key);
    }
}