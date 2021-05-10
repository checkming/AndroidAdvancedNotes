#!/bin/bash

error() {
    echo -e "\n\033[31m** ERROR: $1\033[0m\n" && exit 1
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
        error "Only root user can install artifactory as a service"
    fi

    if [ "$0" = "." ] || [ "$0" = "source" ]; then
        error "Cannot execute script with source $0"
    fi
}

shutdownService() {
    if [ -f ${artServiceFile} ]; then
        SERVICE_STATUS="$(${artServiceFile} status)"
        if [[ ! "$SERVICE_STATUS" =~ .*[sS]topped.* ]]; then
            echo
            echo -n "Stopping the artifactory service..."
            ${artServiceFile} stop || exit $?
            echo -e " DONE"
        fi
    fi
    if [ -f ${artSystemdFile} ]; then
        SERVICE_STATUS="$(systemctl status artifactory.service)"
        if [[ ! "$SERVICE_STATUS" =~ .*[sS]topped.* ]]; then
            echo
            echo -n "Stopping the artifactory service..."
            systemctl stop artifactory.service || exit $?
            echo -e " DONE"
        fi
    fi
}

uninstallInitdService() {
  if [ -x /usr/sbin/update-rc.d ]; then
    /usr/sbin/update-rc.d -f ${serviceInitName} remove &>/dev/null && removeOk=true
  elif [ -x /usr/sbin/chkconfig ]; then
    /usr/sbin/chkconfig --del ${serviceInitName} &>/dev/null && removeOk=true
  elif [ -x /sbin/chkconfig ]; then
    /sbin/chkconfig --del ${serviceInitName} &>/dev/null && removeOk=true
  fi

  [ ${removeOk} ] || error "Could not uninstall service"

  removeOk=
  if [ -x ${artServiceFile} ]; then
    rm -f ${artServiceFile} && removeOk=true
    rm -f ${artServiceFile}.disable &>/dev/null
  fi

  [ ${removeOk} ] || error "Could not delete $artServiceFile"

  removeOk=
  rm ${TOMCAT_HOME}/bin/setenv.sh && removeOk=true

  [ ${removeOk} ] || error "Could not delete $TOMCAT_HOME/bin/setenv.sh"
  echo -e " DONE"
}

uninstallSystemdService() {
  systemctl disable ${serviceSystemdName} &>/dev/null || error "Could not uninstall service"

  if [ -f ${artSystemdFile} ]; then
    rm -f ${artSystemdFile} || error "Could not delete $artSystemdFile"
  fi

  if [ -f ${artSystemdLibFile} ]; then
    rm -f ${artSystemdLibFile} || error "Could not delete $artSystemdLibFile"
  fi

  rm -f ${TOMCAT_HOME}/bin/setenv.sh || error "Could not delete $TOMCAT_HOME/bin/setenv.sh"
  echo -e " DONE"
}

uninstallService() {
    echo -n "Removing the artifactory service from auto-start..."
    # Check if init.d service was installed
    if [ -f ${artServiceFile} ]; then
        uninstallInitdService
    fi
    # Check if systemd service was installed
    if [ -f ${artSystemdFile} ]; then
        uninstallSystemdService
    fi
}

createBackup() {
  # if some files in data move them to a backup folder
  if [ -d "$ARTIFACTORY_HOME/data" ]; then
    TIMESTAMP=$(echo "$(date '+%T')" | tr -d ":")
    CURRENT_TIME="$(date '+%Y%m%d').$TIMESTAMP"
    BACKUP_DIR="${ARTIFACTORY_HOME}/artifactory.backup.${CURRENT_TIME}"

    echo -n "Creating a backup of the artifactory home folder in ${BACKUP_DIR}..."
    mkdir -p "${BACKUP_DIR}" && \
    mv ${artEtcDir} "${BACKUP_DIR}/etc" && \
    mv ${ARTIFACTORY_HOME}/data "${BACKUP_DIR}/data" && \
    mv ${ARTIFACTORY_HOME}/logs "${BACKUP_DIR}/logs" && \
    rm -rf "${BACKUP_DIR}/data/tmp" && \
    rm -rf "${BACKUP_DIR}/data/work" || exit $?
    if [ -e ${TOMCAT_HOME}/lib/mysql-connector-java*.jar ]; then
      echo "MySQL connector found"
      mv ${TOMCAT_HOME}/lib/mysql-connector-java* "${BACKUP_DIR}" || exit $?
    fi
    if [ -e ${ARTIFACTORY_HOME}/backup ]; then
      mv ${ARTIFACTORY_HOME}/backup "${BACKUP_DIR}/backup" || exit $?
    fi
    echo -e " DONE"
  fi
}

removeArtUser() {
  echo -n "Logging off user $ARTIFACTORY_USER..."
  pkill -KILL -u ${ARTIFACTORY_USER}

  rm -rf ${ARTIFACTORY_HOME}/work/* || exit $?

  # Ignoring user folders since the home dir is deleted already by the RPM spec
  echo -n "Removing user $ARTIFACTORY_USER..."
  userdel ${ARTIFACTORY_USER} || exit $?

  EXISTING_GROUP="$(grep $artGroup /etc/group | awk -F ':' '{ print $1 }' 2>/dev/null)"
  if [ "$EXISTING_GROUP" == "$artGroup" ]; then
    echo "Removing group $artGroup"
    groupdel ${artGroup}
  fi
  echo -e " DONE"
}

removeResources() {
  rm -rf ${artEtcDir} && \
  rm -rf ${ARTIFACTORY_HOME}/etc && \
  mv ${ARTIFACTORY_HOME}/etc.original ${ARTIFACTORY_HOME}/etc && \
  removeOk=true

  [ ${removeOk} ] || error "Could not restore etc directory"

  rm -rf ${TOMCAT_HOME}/logs
  if [ -d ${TOMCAT_HOME}/logs.original ]; then
    mv ${TOMCAT_HOME}/logs.original ${TOMCAT_HOME}/logs
  fi
}

showSummary() {
    echo
    echo -e "\033[33mUninstallation of Artifactory completed\033[0m"
    echo -e "Please change the permissions of $ARTIFACTORY_HOME"
}

#
artBinDir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
artExtractDir="$(cd "$(dirname "$artBinDir")" && pwd)"
[ -n "$artServiceFile" ] || artServiceFile="/etc/init.d/artifactory"
[ -n "$artSystemdFile" ] || artSystemdFile="/etc/systemd/system/artifactory.service"
serviceInitName=$(basename ${artServiceFile})
serviceSystemdName=$(basename ${artSystemdFile})
artSystemdLibFile="/lib/systemd/system/artifactory.service"
artDefaultFile="/etc/opt/jfrog/artifactory/default"
[ -n "$artEtcDir" ] || artEtcDir="/etc/opt/jfrog/artifactory"

checkRoot

. ${artDefaultFile} || error "$artDefaultFile does not exist or not executable"

artGroup="$ARTIFACTORY_USER"

shutdownService

uninstallService

createBackup

removeArtUser

removeResources

showSummary
