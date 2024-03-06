package com.rabentall.processdash_reporting;

import java.util.Properties;
import net.sourceforge.processdash.api.PDashContext;
import net.sourceforge.processdash.Settings;

/**
 * \brief Returns the dashboard's settings.
 */
public class DashboardSettings extends DashDataList
{
  Properties dashboardSettings;

  @Override
  void load(PDashContext ctx) {
     dashboardSettings = Settings.getSettings();
  }

  void addElement(Object[] row){
    assert false : "Invalid operation - addElement not defined for DashboardSettings";
  }

}
