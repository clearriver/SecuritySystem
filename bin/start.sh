#!/bin/bash
export JAVA_HOME=/opt/jdk1.8.0_192
export JAVA_BIN=$JAVA_HOME/bin
export JAVA_LIB=$JAVA_HOME/lib
export CLASSPATH=.:$JAVA_LIB/tools.jar:$JAVA_LIB/dt.jar
export PATH=$JAVA_BIN:$PATH

 java -jar web.war  > console.log  2>&1 &  
 
