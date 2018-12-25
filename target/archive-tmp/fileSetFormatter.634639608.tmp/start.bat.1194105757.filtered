@echo off
set  CURRENT=%cd%
cd ..
SET EARLYWARNING_HOME=%cd%
cd %CURRENT%
set LOG4J_LOGPATH=%EARLYWARNING_HOME%\logs
set CONFPATH=%EARLYWARNING_HOME%\conf
set RUNJAR=%EARLYWARNING_HOME%/lib/datahub-1.0.0-SNAPSHOT.jar
rem some properties which need by app
set PROPERTIES_LOG4J_LOGPATH=log4j.logPath
rem run!
@echo on
@java -Xbootclasspath/a:%CONFPATH% -Djava.rmi.server.hostname=localhost -D%PROPERTIES_LOG4J_LOGPATH%=%LOG4J_LOGPATH% -jar %RUNJAR%