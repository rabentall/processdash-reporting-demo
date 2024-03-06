package com.rabentall.processdash_reporting;
import java.util.Map;
import java.util.HashMap;

/**
 * \brief Specialisation of DashData that returns a map of DashDataElements.
 */
abstract class DashDataMap<T> extends DashData {

    /**
     * \brief The map of DashDataElements
     */
    protected Map<Integer, T> elements = new HashMap<Integer, T>();

}
