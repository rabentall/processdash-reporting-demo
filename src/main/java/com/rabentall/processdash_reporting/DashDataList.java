package com.rabentall.processdash_reporting;
import java.util.List;
import java.util.ArrayList;

/**
 * \brief Specialisation of DashData that returns a list of DashDataElements.
 */
abstract class DashDataList extends DashData {

    /**
     * \brief The list of DashDataElements
     */
    protected List<DashDataElement> elements = new ArrayList<DashDataElement>();
}
