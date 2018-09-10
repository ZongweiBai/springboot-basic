#!/bin/bash
#################
# Component:        admin-server
# Required-Start:   $all
# Required-Stop:    $all
# Default-Start:    2 3 4 5
# Default-Stop:     0 1 6
# Description:      Admin Server
# chkconfig: 345 90 10
##################

NAME=springboot-basic-admin-server
CMD=$1
TARGET_PATH=/var/lib/modules/Admin-Server
APP_PATH=$TARGET_PATH/webapp/$NAME.jar

# JVM Args
JVM_XMS_IDP=512m
JVM_XMX_IDP=5128m
JVM_MetaspaceSize_IDP=256m
JVM_MaxMetaspaceSize_IDP=256m

DEFAULT_PORT=9999
DEFAULT_DEBUG_PORT=8998
IAM_IDPPROXY_CONFIG=/var/lib/modules/Admin-Server/config
IAM_LOG_CONFIG=/var/lib/modules/Admin-Server/log
IAM_IDPPROXY_CLASSPATH=/var/lib/modules/Admin-Server/lib

start() {
        echo "Starting $NAME ..."
        if [ ! -d $TARGET_PATH/log/ ]; then
            mkdir $TARGET_PATH/log/
        fi
        if [ ! -f $TARGET_PATH/log/nohup.out ]; then
            echo "" > $TARGET_PATH/log/nohup.out
        fi
        if [ ! -d $TARGET_PATH/static ]; then
            mkdir $TARGET_PATH/static/
        fi
        nohup java -XX:MetaspaceSize=$JVM_MetaspaceSize_IDP -XX:MaxMetaspaceSize=$JVM_MaxMetaspaceSize_IDP -Xms$JVM_XMS_IDP -Xmx$JVM_XMX_IDP -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$DEFAULT_DEBUG_PORT -Dserver.port=$DEFAULT_PORT -DIAM_IDPPROXY_CONFIG=$IAM_IDPPROXY_CONFIG -DIAM_LOG_CONFIG=$IAM_LOG_CONFIG -DIAM_IDPPROXY_CLASSPATH=$IAM_IDPPROXY_CLASSPATH $APP_PATH > $TARGET_PATH/log/nohup.out 2>&1 &
        echo "java -XX:MetaspaceSize=$JVM_MetaspaceSize_IDP -XX:MaxMetaspaceSize=$JVM_MaxMetaspaceSize_IDP -Xms$JVM_XMS_IDP -Xmx$JVM_XMX_IDP -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$DEFAULT_DEBUG_PORT -Dserver.port=$DEFAULT_PORT -DIAM_IDPPROXY_CONFIG=$IAM_IDPPROXY_CONFIG -DIAM_LOG_CONFIG=$IAM_LOG_CONFIG -DIAM_IDPPROXY_CLASSPATH=$IAM_IDPPROXY_CLASSPATH $APP_PATH > $TARGET_PATH/log/nohup.out 2>&1 &"
        sleep 5
        PID=`ps -ef | grep $APP_PATH | grep java | grep -v grep | awk '{print $2}'`
        echo "$NAME started with PID $PID"
}

stop() {
        PID=`ps -ef | grep $APP_PATH | grep java | grep -v grep | awk '{print $2}'`
        echo "Stopping $NAME ($PID) ..."
        kill -9 $PID
        RETVAL=3
        sleep 5
}

status() {
        PID=`ps -ef | grep $APP_PATH | grep java | grep -v grep | awk '{print $2}'`
        if [ -n "$PID" ]; then
          echo -e "$NAME is running with PID $PID\t\e[1;32mPASSED\e[0m"
        else
          echo -e "$NAME is not running \t\t\e[1;31mFAILED\e[0m"
        fi
}

restart() {
        echo "Restarting $NAME ..."
        stop
        start
        status
}

version() {
        java -jar $APP_PATH -version
}

case "$CMD" in
        start)
                start
                ;;
        stop)
                stop
                ;;
        restart)
                restart
                ;;
        status)
                status
                ;;
        version)
                version
                ;;
        *)
                echo "Usage $0 {start|stop|restart|status|version}"
esac

