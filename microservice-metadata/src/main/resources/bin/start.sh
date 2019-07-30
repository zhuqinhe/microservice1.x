#!/bin/sh
# chkconfig: 2345 90 15
# description: sync-rest service
#
APP_NAME=syncrest
APP_HOME=/opt/fonsview/NE/sync-data/$APP_NAME
#export CLASSPATH=$JRE_PATH/lib/:$CLASSPATH
#export PATH=$JRE_PATH/bin:$PATH
#chmod 755 $JRE_PATH/bin/java
#
command="java -jar $APP_HOME/$APP_NAME.jar"
#command="java -jar -Dspring.config.location=/opt/fonsview/NE/sync-data/$APP_NAME/config/application-dev.properties  $APP_HOME/$APP_NAME.jar"

#start app
start(){
      logPid=`ps -ef | grep "$command" | awk '{print $2}' `
      if [ ! -z "${logPid}" ]; then
      for pid in "$logPid";
       do
         C_PID=$(ps --no-heading $pid | wc -l)
         if [ "$C_PID" == "1" ]; then
           echo "$APP_NAME has started,do not start repeat... "
           exit 1
         fi
       done
      fi
      sleep 2
      now=$(date +%Y%m%d)
      echo "$now start"
      exec $command  2>&1 > $APP_HOME/logs/$APP_NAME.log &
     echo "$now start success."
}

start