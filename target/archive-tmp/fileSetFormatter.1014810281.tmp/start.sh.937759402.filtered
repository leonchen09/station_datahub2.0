#!/bin/bash
#create by neo
#获取执行上一级目录，目的是为了获取程序的根目�?
EARLYWARNING_HOME=$(dirname $(pwd))
LOG4J_LOGPATH=$EARLYWARNING_HOME/logs
CONFPATH=$EARLYWARNING_HOME/conf
RUNJAR=$EARLYWARNING_HOME/lib/datahub-1.0.0-SNAPSHOT.jar
#some properties which need by app
PROPERTIES_LOG4J_LOGPATH=log4j.logPath
#run!
#命令结尾的`&`表示后台运行程序
echo "----- begin start datahub -----"
nohup java -Xms128m -Xmx2048m -Xbootclasspath/a:$CONFPATH -D$PROPERTIES_LOG4J_LOGPATH=$LOG4J_LOGPATH -jar $RUNJAR &

sleep 10s

state=`jps|grep datahub`

if [ "${state}" = "" ]; then
    echo "----- start datahub failed -----"
    echo "----- see the reason in ${EARLYWARNING_HOME}/logs/datahub.log"
	exit 1
else
    echo "----- start datahub success -----"
	exit 0
fi
