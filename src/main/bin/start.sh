#!/bin/bash
#获取执行上一级目录，目的是为了获取程序的根目录
EARLYWARNING_HOME=$(dirname $(pwd))
LOG4J_LOGPATH=$EARLYWARNING_HOME/logs
CONFPATH=$EARLYWARNING_HOME/conf
RUNJAR=$EARLYWARNING_HOME/lib/datahub-1.0.0-SNAPSHOT.jar
#some properties which need by app
PROPERTIES_LOG4J_LOGPATH=log4j.logPath
#run!
#命令结尾的`&`表示后台运行程序
echo "----- begin start datahub -----"
#java -Xms128m -Xmx2048m -Xbootclasspath/a:$CONFPATH -D$PROPERTIES_LOG4J_LOGPATH=$LOG4J_LOGPATH -javaagent:/home/datahub/tprofiler/tprofiler-1.0.1.jar -Dprofile.properties=/home/datahub/tprofiler/profile.properties -jar $RUNJAR &
java -Xms128m -Xmx2048m -Xbootclasspath/a:${CONFPATH} -D${PROPERTIES_LOG4J_LOGPATH}=${LOG4J_LOGPATH} -jar ${RUNJAR} &

sleep 10s

state=`jps|grep datahub`

if [ "X${state}" = "X" ]; then
    echo "----- start datahub failed -----"
    echo "----- see the reason in ${EARLYWARNING_HOME}/logs/datahub.log"
	exit 1
else
    echo "----- start datahub success -----"
fi

pid=`ps -ef | grep datahub-1.0.0-SNAPSHOT.jar | grep -v grep | awk '{print $2}'`

mkdir -p ${EARLYWARNING_HOME}/pid

echo ${pid} > ${EARLYWARNING_HOME}/pid/pid

exit 0