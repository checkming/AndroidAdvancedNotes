#!/bin/bash

SERVICE_TYPE=""

errorArtHome() {
    echo
    echo -e "\033[31m** ERROR: $1\033[0m"
    echo
    exit 1
}

checkRoot() {
    curUser=
    if [ -x "/usr/xpg4/bin/id" ]
    then
        curUser=$(/usr/xpg4/bin/id -nu)
    else
        curUser=$(id -nu)
    fi
    if [ "$curUser" != "root" ]
    then
        errorArtHome "Only root user can install artifactory as a service"
    fi

    if [ "$0" = "." ] || [ "$0" = "source" ]; then
        errorArtHome "Cannot execute script with source $0"
    fi
}

getArtUser() {
    if [ -n "$1" ]; then
        ARTIFACTORY_USER=$1
    fi
    if [ -z "$ARTIFACTORY_USER" ]; then
        ARTIFACTORY_USER=artifactory
    fi
}

createArtUser() {
    echo -n "Creating user ${ARTIFACTORY_USER}..."
    artifactoryUsername=$(getent passwd ${ARTIFACTORY_USER} | awk -F: '{print $1}')
    if [ "$artifactoryUsername" = "${ARTIFACTORY_USER}" ]; then
        echo -n "already exists..."
    else
        echo -n "creating..."
        useradd -M -s /usr/sbin/nologin ${ARTIFACTORY_USER}
        if [ ! $? ]; then
            errorArtHome "Could not create user ${ARTIFACTORY_USER}"
        fi
    fi
    echo " DONE"
}

getArtGroup() {
    if [ -n "$1" ]; then
        ARTIFACTORY_GROUP=$1
    fi
}

createArtGroup() {
    [ "${ARTIFACTORY_GROUP}" == "" ] && return 0;
    echo -n "Creating Group ${ARTIFACTORY_GROUP}..."
    artifactoryGroupname=$(getent group ${ARTIFACTORY_GROUP} | awk -F: '{print $1}')
    if [ "$artifactoryGroupname" = "${ARTIFACTORY_GROUP}" ]; then
        echo -n "already exists..."
    else
        echo -n "creating..."
        groupadd ${ARTIFACTORY_GROUP}
        if [ ! $? ]; then
            errorArtHome "Could not create Group ${ARTIFACTORY_GROUP}"
        fi
    fi
    echo " DONE"
}

createArtEtc() {
    echo
    echo -n "Checking configuration link and files in $artEtcDir..."
    if [ -L ${ARTIFACTORY_HOME}/etc ]; then
        echo -n "already exists, no change..."
    else
        echo
        echo -n "Moving configuration dir $artExtractDir/etc $artExtractDir/etc.original..."
        mv ${artExtractDir}/etc ${artExtractDir}/etc.original || \
            errorArtHome "Could not move $artExtractDir/etc $artExtractDir/etc.original"
        if [ ! -d ${artEtcDir} ]; then
            mkdir -p ${artEtcDir} || errorArtHome "Could not create $artEtcDir"
        fi
        echo -n "creating the link and updating dir..."
        ln -s ${artEtcDir} ${ARTIFACTORY_HOME}/etc && \
        cp -R ${artExtractDir}/etc.original/* ${artEtcDir} && \
        etcOK=true
        [ ${etcOK} ] || errorArtHome "Could not create $artEtcDir"
    fi
    echo -e " DONE"
}

createArtDefault() {
    echo -n "Creating environment file $artDefaultFile..."
    if [ -e ${artDefaultFile} ]; then
        echo -n "already exists, no change..."
    else
        # Populating the /etc/opt/jfrog/artifactory/default with ARTIFACTORY_HOME and ARTIFACTORY_USER
        echo -n "creating..."
        cat ${ARTIFACTORY_HOME}/bin/artifactory.default > ${artDefaultFile} && \
        echo "" >> ${artDefaultFile}

        sed --in-place -e "
            s,#export ARTIFACTORY_HOME=.*,export ARTIFACTORY_HOME=${ARTIFACTORY_HOME},g;
            s,#export ARTIFACTORY_USER=.*,export ARTIFACTORY_USER=${ARTIFACTORY_USER},g;
            s,export TOMCAT_HOME=.*,export TOMCAT_HOME=${TOMCAT_HOME},g;
            s,export $ARTIFACTORY_PID=.*,export $ARTIFACTORY_PID=${artRunDir}/artifactory.pid,g;" ${artDefaultFile} || \
                errorArtHome "Could not change values in $artDefaultFile"
    fi
    echo -e " DONE"
    echo -e "\033[33m** INFO: Please edit the files in $artEtcDir to set the correct environment\033[0m"
    echo -e "\033[33mEspecially $artDefaultFile that defines ARTIFACTORY_HOME, JAVA_HOME and JAVA_OPTIONS\033[0m"
}

createArtRun() {
    # Since tomcat 6.0.24 the PID file cannot be created before running catalina.sh. Using /var/opt/jfrog/artifactory/run folder.
    if [ ! -d "$artRunDir" ]; then
        mkdir -p "$artRunDir" || errorArtHome "Could not create $artRunDir"
    fi
}

checkServiceStatus() {
    # Shutting down the artifactory service if running
    if [ -e "$artInitdFile" ]; then
        SERVICE_STATUS="$(${artInitdFile} status)"
        if [[ ! "$SERVICE_STATUS" =~ .*[sS]topped.* ]]; then
            echo "Stopping the artifactory service..."
            ${artInitdFile} stop || exit $?
        fi
    fi

    if [ -e "$artSystemdFile" ]; then
        SERVICE_STATUS="$(systemctl status artifactory.service)"
        if [[ ! "$SERVICE_STATUS" =~ .*[sS]topped.* ]]; then
            echo "Stopping the artifactory service..."
            systemctl stop artifactory.service || exit $?
        fi
    fi
}

installInitdService() {
    if [ -e ${artSystemdFile} ]; then
        rm -f ${artSystemdFile}
    fi
    serviceFiles=${artBinDir}/../misc/service
    if [ -e "$artInitdFile" ]; then
        cp -f ${artInitdFile} ${serviceFiles}/${serviceInitName}.disabled
    fi
    cp -f ${serviceFiles}/artifactory ${artInitdFile}
    chmod a+x ${artInitdFile}

    # Change pidfile and default location if needed
    sed --in-place -e "
     /processname:/ s%artifactory%$serviceInitName%g;
     /Provides:/ s%artifactory%$serviceInitName%g;
     s%# pidfile: .*%# pidfile: $artRunDir/artifactory.pid%g;
     s%/etc/opt/jfrog/artifactory/default%$artEtcDir/default%g;
     " ${artInitdFile} || errorArtHome "Could not change values in $artInitdFile"

    # Try update-rc.d for debian/ubuntu else use chkconfig
    if [ -x /usr/sbin/update-rc.d ]; then
        echo
        echo -n "Initializing artifactory service with update-rc.d..."
        update-rc.d ${serviceInitName} defaults && \
        chkconfigOK=true
    elif [ -x /usr/sbin/chkconfig ] || [ -x /sbin/chkconfig ]; then
        echo
        echo -n "Initializing $serviceInitName service with chkconfig..."
        chkconfig --add ${serviceInitName} && \
        chkconfig ${serviceInitName} on && \
        chkconfig --list ${serviceInitName} && \
        chkconfigOK=true
    else
        ln -s ${artInitdFile} /etc/rc3.d/S99${serviceInitName} && \
        chkconfigOK=true
    fi
    [ ${chkconfigOK} ] || errorArtHome "Could not install artifactory service"
    echo -e " DONE"
    SERVICE_TYPE="init.d"
}

installSystemdService() {
    if [ -e ${artInitdFile} ]; then
        mv ${artInitdFile} ${artInitdFile}.disabled
    fi
    serviceFiles=${artBinDir}/../misc/service
    if [ -e "${artSystemdFile}" ]; then
        mv ${artSystemdFile} ${serviceFiles}/${serviceSystemdName}.disabled
    fi
    cp -f ${serviceFiles}/artifactory.service ${artSystemdFile}
    chmod a+x ${artSystemdFile}

    # Edit artifactory.service for artifactoryManage.sh script location
    sed -i "s|ExecStart=\/opt\/jfrog\/artifactory\/bin\/artifactoryManage.sh start|ExecStart=${artBinDir}\/artifactoryManage.sh start|g; \
    s|ExecStop=\/opt\/jfrog\/artifactory\/bin\/artifactoryManage.sh stop|ExecStop=${artBinDir}\/artifactoryManage.sh stop|g; \
    s|PIDFile=\/var\/opt\/jfrog\/run\/artifactory.pid|PIDFile=${artExtractDir}\/run\/artifactory.pid|g;" ${artSystemdFile}

    # Use the systemctl command to enable the service
    echo -n "Initializing $serviceSystemdName service with systemctl..."
    systemctl daemon-reload &>/dev/null
    systemctl enable artifactory &>/dev/null
    systemctl list-unit-files --type=service | grep artifactory.service &>/dev/null || errorArtHome "Could not install artifactory service"
    echo -e " DONE"
    SERVICE_TYPE="systemd"
}

installService() {
    checkServiceStatus
    # Distribution-specific logic
    systemctl -h > /dev/null 2>&1
    if [[ $? -eq 0 ]]; then
        # Installing service from type systemd
        installSystemdService
    else
        # Installing service from type init.d
        installInitdService
    fi
}

prepareTomcat() {
    cp ${serviceFiles}/setenv.sh ${TOMCAT_HOME}/bin/setenv.sh && \
     sed --in-place -e "
      s%/etc/opt/jfrog/artifactory/default%$artEtcDir/default%g;
      " ${TOMCAT_HOME}/bin/setenv.sh && \
      chmod a+x ${TOMCAT_HOME}/bin/* || errorArtHome "Could not set the $TOMCAT_HOME/bin/setenv.sh"

    if [ ! -L "$TOMCAT_HOME/logs" ]; then
        if [ -d $TOMCAT_HOME/logs ]; then
            mv ${TOMCAT_HOME}/logs ${TOMCAT_HOME}/logs.original
            mkdir ${TOMCAT_HOME}/logs
        fi
        mkdir -p ${artLogDir}/catalina || errorArtHome "Could not create dir $artLogDir/catalina"
        ln -s ${artLogDir}/catalina ${TOMCAT_HOME}/logs && \
        chmod -R u+w ${TOMCAT_HOME}/logs && \
        logOK=true
        [ ${logOK} ] || errorArtHome "Could not create link from $TOMCAT_HOME/logs to $artLogDir/catalina"
    fi
    if [ ! -d ${TOMCAT_HOME}/temp ];then
        mkdir ${TOMCAT_HOME}/temp
    fi
    chmod -R u+w ${TOMCAT_HOME}/work
}

setPermissions() {
    echo
    echo -n "Setting file permissions..."
    chown -RL ${ARTIFACTORY_USER}:${ARTIFACTORY_GROUP} ${ARTIFACTORY_HOME} || errorArtHome "Could not set permissions"
    echo -e " DONE"
}

showSummary() {
    echo
    echo -e "\033[33m************ SUCCESS ****************\033[0m"
    echo -e "\033[33mInstallation of Artifactory completed\033[0m"
    echo
    echo "Please check $artEtcDir, $TOMCAT_HOME and $ARTIFACTORY_HOME folders"
    echo

    if [ ${SERVICE_TYPE} == "init.d" ]; then
        echo "You can now check installation by running:"
        echo "> service artifactory check (or $artInitdFile check)"
        echo
        echo "Then activate artifactory with:"
        echo "> service artifactory start (or $artInitdFile start)"
    fi

    if [ ${SERVICE_TYPE} == "systemd" ]; then
        echo "You can activate artifactory with:"
        echo "> systemctl start artifactory.service"
    fi
}

##
checkRoot

artBinDir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
artExtractDir="$(cd "$(dirname "$artBinDir")" && pwd)"

[ -n "$ARTIFACTORY_HOME" ] || ARTIFACTORY_HOME="$artExtractDir"
[ -n "$artEtcDir" ] || artEtcDir="/etc/opt/jfrog/artifactory"
TOMCAT_HOME="$ARTIFACTORY_HOME/tomcat"
artLogDir="$ARTIFACTORY_HOME/logs"
artRunDir="$ARTIFACTORY_HOME/run"
[ -n "$artInitdFile" ] || artInitdFile="/etc/init.d/artifactory"

if [ -z "$artSystemdFile" ]; then
    SUSE=$(cat /etc/issue | grep -io suse)
    NOSUSE=$(echo $SUSE | tr "[:upper:]" "[:lower:]")
    UNAME=$(uname | tr "[:upper:]" "[:lower:]")
    if [ "$UNAME" == "linux" ] && [ "$NOSUSE" != "suse" ]; then
        artSystemdFile="/lib/systemd/system/artifactory.service"
    else
        artSystemdFile="/usr/lib/systemd/system/artifactory.service"
    fi
fi

serviceInitName=$(basename ${artInitdFile})
serviceSystemdName=$(basename ${artSystemdFile})

artDefaultFile="$artEtcDir/default"

getArtUser "$1"
getArtGroup "$2"

echo
echo "Installing artifactory as a Unix service that will run as user ${ARTIFACTORY_USER}${ARTIFACTORY_GROUP:+ and group $ARTIFACTORY_GROUP}"
echo "Installing artifactory with home ${ARTIFACTORY_HOME}"

createArtUser

createArtGroup

createArtEtc

createArtDefault

createArtRun

installService

prepareTomcat

setPermissions

showSummary
