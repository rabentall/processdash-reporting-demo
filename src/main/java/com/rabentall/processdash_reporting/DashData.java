package com.rabentall.processdash_reporting;
import java.util.List;

import net.sourceforge.processdash.api.PDashContext;
import net.sourceforge.processdash.api.PDashQuery;

abstract class DashData {

    private transient PDashContext ctx_;
    
    DashData(PDashContext ctx){
        ctx_ = ctx;
    }

    protected List<Object[]> getRows(String hql){

         List<Object[]> rows = ctx_.getQuery().query(hql, PDashQuery.FilterMode.CURRENT);

        System.out.println("**** ROWCOUNT:" + rows.size());

        return rows;
    }

    abstract void load();

}