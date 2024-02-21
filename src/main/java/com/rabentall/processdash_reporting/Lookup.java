package com.rabentall.processdash_reporting;
import java.util.Map;

import net.sourceforge.processdash.api.PDashContext;

import java.util.HashMap;

class Lookup<T> extends DashData {

    String hql_;

    Lookup(String hql){
        hql_ = hql;
    }

    private Map<Integer, T> elements = new HashMap<Integer, T>();

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
