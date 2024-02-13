#!/bin/bash

# Add pspdash.jar to local repo:



mvn install:install-file -Dfile=/c/Work/Git/pdash/processdash/dist/pspdash.jar -DgroupId=pdash -DartifactId=pspdash -Dversion=2.6.11 -Dpackaging=jar