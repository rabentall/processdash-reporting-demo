package com.rabentall.processdash_reporting;

import java.util.Properties;
import net.sourceforge.processdash.api.PDashContext;
import net.sourceforge.processdash.Settings;

public class DashboardSettings extends DashDataList
{
  Properties dashboardSettings = Settings.getSettings();

  @Override
  void load(PDashContext ctx) {
  }


  void addElement(Object[] row){}

}
