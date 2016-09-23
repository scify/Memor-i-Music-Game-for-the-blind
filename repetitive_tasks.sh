#!/bin/bash
# This script executes some repetitive tasks required to compile this application

#The following command clears the config.properties file (To clear any saved highscores)
echo -n "" > src/main/resources/config.properties

#Maven related tasks

mvn clean

mvn package

mvn assembly:single

mvn dependency:copy-dependencies
