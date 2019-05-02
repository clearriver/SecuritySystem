@echo off
%1 mshta vbscript:CreateObject("WScript.Shell").Run("%~s0 ::",0,FALSE)(window.close)&&exit

java -jar bin/ss-h2-2.0.6.RELEASE.jar --devMode=false 