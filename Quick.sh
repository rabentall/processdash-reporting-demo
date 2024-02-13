#!/bin/bash

# Copy across javascript (unpacked) so don't need to restart dashboard:

rm -r "$HOME/AppData/Roaming/Process Dashboard/Templates/personal"
mkdir "$HOME/AppData/Roaming/Process Dashboard/Templates/personal"

cp ./src/main/webapp/personal/*.* "$HOME/AppData/Roaming/Process Dashboard/Templates/personal"

rm -r "$HOME/AppData/Roaming/Process Dashboard/Templates/script"
mkdir "$HOME/AppData/Roaming/Process Dashboard/Templates/script"
cp ./src/main/webapp/script/*.js "$HOME/AppData/Roaming/Process Dashboard/Templates/script"

cp ./src/main/webapp/style/*.css "$HOME/AppData/Roaming/Process Dashboard/Templates/style"