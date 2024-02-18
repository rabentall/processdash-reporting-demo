package com.rabentall.processdash_reporting;
import java.util.List;
import java.util.ArrayList;

abstract class DashDataList extends DashData {
    protected List<DashDataElement> elements = new ArrayList<DashDataElement>();
}
