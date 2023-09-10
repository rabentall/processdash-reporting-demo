#!/bin/bash

#
# Use to build and deploy plugin:
#

mvn clean package

# Copy across to templates folder:
cp ./target/*.war "$HOME/AppData/Roaming/Process Dashboard/Templates"

