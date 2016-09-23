#!/bin/bash
# This script executes some repetitive tasks required to compile this application

mvn clean

mvn package

mvn assembly:single

mvn dependency:copy-dependencies
