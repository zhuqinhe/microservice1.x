#!/bin/bash
APP_NAME=syncrest

ps -ef|grep "$APP_NAME.jar" |grep -v grep| awk '{print "kill -9 " $2}' |sh

if [ $? == 0 ]; then 
  echo 'syncrest stop success.' 
  exit 0
else
  echo 'syncrest stop failure!!!' 
  exit 1
fi

