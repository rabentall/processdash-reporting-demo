#!/bin/bash

#
# Use to build and deploy plugin:
#

mvn clean package

# Copy across to templates folder:
cp ./target/*.war "$HOME/AppData/Roaming/Process Dashboard/Templates"

# Copy across javascript (unpacked) so don't need to restart dashboard:
#cp ./src/main/webapp/activityTracker.jsp "$HOME/AppData/Roaming/Process Dashboard/Templates"
#cp ./src/main/webapp/script/activityTracker.js "$HOME/AppData/Roaming/Process Dashboard/Templates/script"
./Quick.sh