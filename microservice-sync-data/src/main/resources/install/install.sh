#!/usr/bin/env sh

Force=$1

# atslogstat默认安装路径
APP_NAME="syncrest"
APP_HOME="/opt/fonsview/NE/sync-data/$APP_NAME"


#[ -d /opt/fonsview/NE/sync-data/etc ] || mkdir -p /opt/fonsview/NE/sync-data/etc
#[ -d /opt/fonsview/NE/sync-data/bin ] || mkdir -p /opt/fonsview/NE/sync-data/bin
[ -d ${APP_HOME}/etc ] || mkdir -p ${APP_HOME}/etc
[ -d ${APP_HOME}/lib ] || mkdir -p ${APP_HOME}/lib
[ -d ${APP_HOME}/logs ] || mkdir -p ${APP_HOME}/logs



\cp -R ./../bin ${APP_HOME}
\cp -rf ./../lib/* ${APP_HOME}/lib
\cp ./../sync-data-0.0.1-SNAPSHOT.jar ${APP_HOME}/${APP_NAME}.jar  

if [ "$Force" == "-f" ]; then
       \cp -rf ./../etc/config.properties ${APP_HOME}/etc
	   \cp -rf ./../etc/log4j.xml ${APP_HOME}/etc
	  # \cp -rf ./../etc/application.properties ${APP_HOME}/etc
      # \cp -rf ${APP_HOME}/etc/ehcache.xml ${APP_HOME}/etc/
	  #	\cp -rf ${APP_HOME}/etc/log4j2.xml ${APP_HOME}/etc/
	  # \cp -rf ${APP_HOME}/etc/config.properties ${APP_HOME}/etc/	
fi

chmod +x $APP_HOME/bin/*.sh
chmod +x ${APP_HOME}/${APP_NAME}.jar
#chmod +x /opt/fonsview/NE/sync-data/bin/version.sh


echo "install $APP_NAME -----------OK"
