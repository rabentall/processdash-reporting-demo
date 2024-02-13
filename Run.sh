#!/bin/bash

#
# Use to launch team dashboard
# dashboard=./FilteredMilestonePD
# ;../deploy/pdash/*

classpath="../deploy/pdash/pspdash.jar"
mainclass=net.sourceforge.processdash.tool.quicklauncher.QuickLauncher

dashboard=./TestData/pdash-boilerAssemblyDash-20240201.zip
jvmargs="-Dnet.sourceforge.processdash.disableSecurityManager=true -DquickLauncher.sameJVM=true -Xmx4000m"

java $jvmargs -cp $classpath $mainclass $dashboard