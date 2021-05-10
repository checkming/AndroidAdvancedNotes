#!/bin/bash
#
# Startup script for Artifactory in Tomcat Servlet Engine
#

errorArtHome() {
    echo
    echo -e "\033[31m** $1\033[0m"
    echo
    exit 1
}

checkArtHome() {
    if [ -z "$ARTIFACTORY_HOME" ] || [ ! -d "$ARTIFACTORY_HOME" ]; then
        errorArtHome "ERROR: Artifactory home folder not defined or does not exists at $ARTIFACTORY_HOME"
    fi
}

checkTomcatHome() {
    if [ -z "$TOMCAT_HOME" ] || [ ! -d "$TOMCAT_HOME" ]; then
        errorArtHome "ERROR: Tomcat Artifactory folder not defined or does not exists at $TOMCAT_HOME"
    fi
    export CATALINA_HOME="$TOMCAT_HOME"
}

createLogsLink() {
    mkdir -p $ARTIFACTORY_HOME/logs/catalina || errorArtHome "Could not create dir $ARTIFACTORY_HOME/logs/catalina"
    if [ ! -L "$TOMCAT_HOME/logs" ];
    then
        ln -s $ARTIFACTORY_HOME/logs/catalina $TOMCAT_HOME/logs || \
            errorArtHome "Could not create link from $TOMCAT_HOME/logs to $ARTIFACTORY_HOME/logs/catalina"
    fi
}

findShutdownPort() {
    SHUTDOWN_PORT=`netstat -vatn|grep LISTEN|grep $CATALINA_MGNT_PORT|wc -l`
}

isAlive() {
    pidValue=""
    javaPs=""
    if [ -e "$ARTIFACTORY_PID" ]; then
        pidValue=`cat $ARTIFACTORY_PID`
        if [ -n "$pidValue" ]; then
            javaPs="`ps -p $pidValue | grep java`"
        fi
    fi
}

# Check if conditions to run local replicator are met
runReplicator() {
    if [ "${START_LOCAL_REPLICATOR}" == true ] && [ -f ${replicatorScript} ]; then
        return 0
    else
        return 1
    fi
}

startReplicator() {
    if runReplicator; then
        chmod +x ${replicatorScript}
        ${replicatorScript} start
    fi
}

stopReplicator() {
    if runReplicator; then
        chmod +x ${replicatorScript}
        ${replicatorScript} stop
    fi
}

stop() {
    echo finding
    # The default CATALINA_MGNT_PORT is 8015
    CATALINA_MGNT_PORT=8015
    echo "Using the default catalina management port ($CATALINA_MGNT_PORT) to test shutdown"
    isAlive
    findShutdownPort
    if [ $SHUTDOWN_PORT -eq 0 ] && [ -z "$javaPs" ]; then
        echo "Artifactory Tomcat already stopped"
        RETVAL=0
    else
        echo "Stopping Artifactory Tomcat..."
        if [ $SHUTDOWN_PORT -ne 0 ]; then
            $TOMCAT_HOME/bin/shutdown.sh
            RETVAL=$?
        else
            RETVAL=1
        fi
        killed=false
        if [ $RETVAL -ne 0 ]; then
            echo "WARN: Artifactory Tomcat server shutdown script failed. Sending kill signal to $pidValue"
            if [ -n "$pidValue" ]; then
                killed=true
                kill $pidValue
                RETVAL=$?
            fi
        fi
        # Wait 2 seconds for process to die
        sleep 2
        findShutdownPort
        isAlive
        nbSeconds=1
        while [ $SHUTDOWN_PORT -ne 0 ] || [ -n "$javaPs" ] && [ $nbSeconds -lt 30 ]; do
            if [ $nbSeconds -eq 10 ] && [ -n "$pidValue" ]; then
                # After 10 seconds try to kill the process
                echo "WARN: Artifactory Tomcat server shutdown not done after 10 seconds. Sending kill signal"
                kill $pidValue
                RETVAL=$?
            fi
            if [ $nbSeconds -eq 25 ] && [ -n "$pidValue" ]; then
                # After 25 seconds try to kill -9 the process
                echo "WARN: Artifactory Tomcat server shutdown not done after 25 seconds. Sending kill -9 signal"
                kill -9 $pidValue
                RETVAL=$?
            fi
            sleep 1
            let "nbSeconds = $nbSeconds + 1"
            findShutdownPort
            isAlive
        done
        if [ $SHUTDOWN_PORT -eq 0 ] && [ -z "$javaPs" ]; then
           echo "Artifactory Tomcat stopped"
        else
           echo "ERROR: Artifactory Tomcat did not stop"
           RETVAL=1
        fi
    fi
    [ $RETVAL=0 ] && rm -f "$ARTIFACTORY_PID"

    stopReplicator
}

start() {
    export CATALINA_OPTS="$JAVA_OPTIONS -Dartifactory.home=$ARTIFACTORY_HOME -Dfile.encoding=UTF8 -Djruby.bytecode.version=1.8 -Djruby.compile.invokedynamic=false"
    export CATALINA_PID="$ARTIFACTORY_PID"
    [ -x $TOMCAT_HOME/bin/catalina.sh ] || chmod +x $TOMCAT_HOME/bin/*.sh
    if [ -z "$@" ];
    then
        startReplicator

        #default to catalina.sh run
        $TOMCAT_HOME/bin/catalina.sh run
    else
        #create $ARTIFACTORY_HOME/run
        if [ -n "$ARTIFACTORY_PID" ];
        then
            mkdir -p $(dirname "$ARTIFACTORY_PID") || \
            errorArtHome "Could not create dir for $ARTIFACTORY_PID";
        fi
        if [ "$@" == "stop" ];
        then
            stop
        else
            startReplicator

            # Start tomcat
            $TOMCAT_HOME/bin/catalina.sh "$@"
        fi
    fi
}

check() {
    if [ -f $ARTIFACTORY_PID ]; then
        echo "Artifactory is running, on pid="`cat $ARTIFACTORY_PID`
        echo ""
        exit 0
    fi

    echo "Checking arguments to Artifactory: "
    echo "ARTIFACTORY_HOME     =  $ARTIFACTORY_HOME"
    echo "TOMCAT_HOME          =  $TOMCAT_HOME"
    echo "ARTIFACTORY_PID      =  $ARTIFACTORY_PID"
    echo "JAVA_HOME            =  $JAVA_HOME"
    echo "JAVA_OPTIONS         =  $JAVA_OPTIONS"
    echo

    exit 1
}

checkJavaVersion(){
    if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
        echo "Found java executable in JAVA_HOME ($JAVA_HOME)"
        _java="$JAVA_HOME/bin/java"
    elif type -p java; then
        _java=java
    else
        echo "No java found"
    fi

    if [[ "$_java" ]]; then
        "$_java" -version 2>&1| \
        awk -F\" '/version/{\
            if ($2 < 1.8) {\
                printf "%s is too old must be at least java 1.8\n", $2;\
                exit 0;\
            } else exit 1}' && exit 99
    fi
}

# Extra termination steps needed
terminate () {
    echo "Caught termination signal"
    stopReplicator
}

artBinDir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export ARTIFACTORY_HOME="$(cd "$(dirname "${artBinDir}")" && pwd)"
export REPLICATOR_DATA="${ARTIFACTORY_HOME}/replicator"
artDefaultFile="$artBinDir/artifactory.default"
replicatorScript=${artBinDir}/replicator/replicator.sh

. $artDefaultFile || errorArtHome "ERROR: $artDefaultFile does not exist or not executable"

# Support old REPLICATOR_ENABLED
if [ "${REPLICATOR_ENABLED}" == true ]; then
    export START_LOCAL_REPLICATOR=true
fi

if [ "x$1" = "xcheck" ]; then
    check
fi

# Catch Ctrl+C and other termination signals to try graceful shutdown
trap terminate SIGINT SIGTERM SIGHUP

checkJavaVersion
checkArtHome
checkTomcatHome
createLogsLink

start "$@"
