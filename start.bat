@echo off
echo start 

rem start cmd /c "java -jar bin/ss-h2-2.0.6.RELEASE.jar --devMode=false"
rem start cmd /c "java -jar bin/web.war"

echo hava start H2 .

start java -jar bin/ss-h2-2.0.6.RELEASE.jar --devMode=false
start java -jar bin/web.war   --devMode=false

echo hava start web .
pause
