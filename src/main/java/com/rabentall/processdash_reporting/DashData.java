package com.rabentall.processdash_reporting;
import java.util.List;

import net.sourceforge.processdash.api.PDashContext;
import net.sourceforge.processdash.api.PDashQuery;

abstract class DashData {

    protected List<Object[]> getRows(PDashContext ctx, String hql){

         List<Object[]> rows = ctx.getQuery().query(hql, PDashQuery.FilterMode.CURRENT);

        System.out.println("**** ROWCOUNT:" + rows.size());

        return rows;
    }

    abstract void load(PDashContext ctx);

}

enum ActivityStatus{
    TODO,
    WIP,
    COMPLETED,
    UNKNOWN
};