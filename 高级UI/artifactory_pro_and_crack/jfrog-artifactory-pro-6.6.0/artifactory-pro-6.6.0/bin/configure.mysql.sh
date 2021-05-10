#!/bin/bash

error() {
    echo -e "\n\033[31m** ERROR: $1\033[0m\n" && exit 1
}

###
# READ DEFAULT ENV VARS
###

artEtcDefault="/etc/opt/jfrog/artifactory/default"
artBinDir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
artBinDefault="$artBinDir/artifactory.default"

ARTIFACTORY_HOME="$(cd "$(dirname "$artBinDir")" && pwd)" #default to extract dir

if [ -e ${artEtcDefault} ]; then
    . ${artEtcDefault}
elif [ -e ${artBinDefault} ]; then
    . ${artBinDefault}
else
    error "Could not find artifactory default file"
fi

DB_PROPS="$ARTIFACTORY_HOME/etc/db.properties"
MYSQL_PROPS_SERVICE="/opt/jfrog/artifactory/misc/db/mysql.properties"
MYSQL_PROPS_SOURCE="$ARTIFACTORY_HOME/misc/db/mysql.properties"
TOMCAT_LIB="$TOMCAT_HOME/lib"
MYSQL_MIN_VERSION="5.5"
JDBC_VERSION=5.1.24
ROOT_REPO="http://repo.jfrog.org/artifactory/remote-repos"
DEFAULT_DATABASE_USERNAME=artifactory
DEFAULT_DATABASE_PASSWORD=password

# Interactively configures MySQL for use by Artifactory

###
# CHECK FOR ROOT
###

curUser=
if [ -x "/usr/xpg4/bin/id" ]
then
    curUser=$(/usr/xpg4/bin/id -nu)
else
    curUser=$(id -nu)
fi
if [ "$curUser" != "root" ]
then
    error "This tool can only be used when logged in as root."
fi

if [ "$0" = "." ] || [ "$0" = "source" ]; then
    error "Cannot execute script with source $0"
fi

echo
echo "########################################################"
echo "# Welcome to the Artifactory MySQL configuration tool! #"
echo "########################################################"

###
# CHECK FOR MYSQL RUNTIME
###

MYSQL_EXISTS=$(type -P mysql)
if [ -z "$MYSQL_EXISTS" ]; then
    error "Unable to find MySQL. Please make sure it installed and available before continuing."
fi

###
# CHECK THAT NEEDED FILES EXIST
###

# Copy mysql.properties as storage.properties
if [ ! -f "$DB_PROPS" ]; then
    [ -f ${MYSQL_PROPS_SERVICE} ] && cp ${MYSQL_PROPS_SERVICE} ${DB_PROPS} || \
    [ -f ${MYSQL_PROPS_SOURCE} ] && cp ${MYSQL_PROPS_SOURCE} ${DB_PROPS} || \
        error "Could not find mysql.properties"
fi

if [ ! -f "$DB_PROPS" ]; then
    error "Unable to find db.properties at '${DB_PROPS}'. Cannot continue."
fi

if [ ! -d "$TOMCAT_LIB" ]; then
  error "Unable to find Tomcat lib directory at '${TOMCAT_LIB}'. Cannot continue."
fi

###
# CHECK FOR EXISTING STARTUP INIT OR SYSTEMD SCRIPT AND RUNNING THE SERVICE
###

STARTUP_INIT_SCRIPT="/etc/init.d/artifactory"
STARTUP_SYSTEMD_SCRIPT="/etc/systemd/system/artifactory.service"

if [ -f ${STARTUP_INIT_SCRIPT} ]; then
  SERVICE_STATUS="$(${STARTUP_INIT_SCRIPT} status)"
  if [[ ! "$SERVICE_STATUS" =~ .*[sS]topped.* ]]; then
    echo
    echo "Stopping the Artifactory service..."
    ${STARTUP_INIT_SCRIPT} stop || exit $?
  fi
fi

if [ -f ${STARTUP_SYSTEMD_SCRIPT} ]; then
  SERVICE_STATUS="$(systemctl status artifactory.service)"
  if [[ ! "$SERVICE_STATUS" =~ .*[sS]topped.* ]]; then
    echo
    echo "Stopping the Artifactory service..."
    systemctl stop artifactory.service || exit $?
  fi
fi

###
# CHECK FOR EXISTING DATA FOLDER
###

DATA_FOLDER="$ARTIFACTORY_HOME/data"
if [ -d "$DATA_FOLDER" ]; then
  echo
  echo "Please notice: An existing Artifactory data folder has been found at '${DATA_FOLDER}' and can be kept aside."
  read -p "Continue [Y/n]? " CONTINUE_INSTALL
  
  if [[ "${CONTINUE_INSTALL}" =~ [nN] ]]; then
    error "Please make sure to move aside the current data folder before continuing."
  fi

  BACKUP_DATA_FOLDER=${DATA_FOLDER}.backup
  echo
  echo "Moving the Artifactory data folder to '${BACKUP_DATA_FOLDER}'. You may remove it later."
  mv ${DATA_FOLDER} ${BACKUP_DATA_FOLDER} && \
   chown artifactory:artifactory ${BACKUP_DATA_FOLDER} || exit $?
fi

###
# PROMPT FOR MYSQL ADMIN USER
###

echo
read -p "Please enter the MySQL server admin username [root]: " MYSQL_ADMIN_USERNAME
if [ -z "$MYSQL_ADMIN_USERNAME" ]; then
  MYSQL_ADMIN_USERNAME="root"
fi

###
# PROMPT FOR MYSQL ADMIN PASSWORD
###

echo
read -s -p "Please enter the MySQL server admin password: " MYSQL_ADMIN_PASSWORD

###
# CONFIGURE MYSQL
###

echo
read -p "Please enter the Artifactory database username [$DEFAULT_DATABASE_USERNAME]: " ARTIFACTORY_DATABASE_USERNAME
if [ -z "$ARTIFACTORY_DATABASE_USERNAME" ]; then
  ARTIFACTORY_DATABASE_USERNAME="$DEFAULT_DATABASE_USERNAME"
fi

echo
read -s -p "Please enter the Artifactory database password [$DEFAULT_DATABASE_PASSWORD]: " ARTIFACTORY_DATABASE_PASSWORD
if [ -z "$ARTIFACTORY_DATABASE_PASSWORD" ]; then
  ARTIFACTORY_DATABASE_PASSWORD="$DEFAULT_DATABASE_PASSWORD"
fi

echo
echo "Creating the Artifactory MySQL user and database..."

MYSQL_LOGIN="mysql -u$MYSQL_ADMIN_USERNAME"

if [ ! -z "$MYSQL_ADMIN_PASSWORD" ];then
  MYSQL_LOGIN="$MYSQL_LOGIN -p$MYSQL_ADMIN_PASSWORD"
fi

###
# Check MySQL version is at least $MYSQL_MIN_VERSION
###
ver=$(${MYSQL_LOGIN} <<EOF
SELECT VERSION();
EOF
)

if [ $? -ne 0 ]; then
    error "Failed to check MySQL version. Please verify the version is at least $MYSQL_MIN_VERSION"
else
    ver=$(echo $ver | awk '{print $2}' | awk -F'-' {'print $1'})
    if [ "$ver" \< ${MYSQL_MIN_VERSION} ]
    then
        error "Found MySQL version $ver. should be at least $MYSQL_MIN_VERSION"
    fi
fi

${MYSQL_LOGIN} <<EOF
CREATE DATABASE IF NOT EXISTS artdb CHARACTER SET=utf8 COLLATE=utf8_bin;
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER,INDEX on artdb.* TO '${ARTIFACTORY_DATABASE_USERNAME}'@'localhost' IDENTIFIED BY '${ARTIFACTORY_DATABASE_PASSWORD}';
FLUSH PRIVILEGES;
QUIT
EOF

if [ $? -ne 0 ]; then
    error "Failed to execute MySQL setup."
fi

###
# EDIT THE MYSQL CONFIG IF NEEDED
###

## Replace username/password in mysql.properties
if [ "$ARTIFACTORY_DATABASE_USERNAME" != "$DEFAULT_DATABASE_USERNAME" ]; then
  sed -r --in-place "s/username=.*/username=$ARTIFACTORY_DATABASE_USERNAME/g" "$DB_PROPS" || exit $?
fi

if [ "$ARTIFACTORY_DATABASE_PASSWORD" != "$DEFAULT_DATABASE_PASSWORD" ]; then
  sed -r --in-place "s/password=.*/password=$ARTIFACTORY_DATABASE_PASSWORD/g" "$DB_PROPS" || exit $?
fi

###
# DOWNLOAD THE MYSQL CONNECTOR
###

JDBC_JAR=mysql-connector-java-${JDBC_VERSION}.jar

downloadMysqlConnector() {
  if [ -z $(type -P wget) ]; then
    echo
    error "Unable to find wget: Try running \"yum or apt-get install wget\" and then retry the MySQL configuration"
  fi

  echo
  echo "Downloading $JDBC_JAR to $TOMCAT_LIB..."
  RESPONSE=$(wget -nv --timeout=30 -O $TOMCAT_LIB/$JDBC_JAR $ROOT_REPO/mysql/mysql-connector-java/$JDBC_VERSION/$JDBC_JAR 2>&1)
  if [ $? -ne 0 ] || [[ "${RESPONSE}" == *ERROR* ]]; then
    echo
    echo "Error: Unable to download the MySQL JDBC connector. ERROR:"
    echo "$RESPONSE"
    echo "Please place it manually under $TOMCAT_LIB before running Artifactory."
    echo "Press enter to quit..."
    read
    exit 1
  fi
}

copyOrDownloadMysqlConnector() {
  read -p "Does the current server has access to the Internet? [Y/n]" INTERNET_ACCESS
  if [[ "$INTERNET_ACCESS" =~ [nN] ]]; then
    echo
    read -p "Please provide a local path to $JDBC_JAR?" JDBC_JAR_PATH
    cp ${JDBC_JAR_PATH} ${TOMCAT_LIB} || exit $?
  else
    downloadMysqlConnector || exit $?
  fi
}

existingConnector="$(ls -A $TOMCAT_LIB | grep "mysql-connector-java-")"
if [ "$existingConnector" == "$JDBC_JAR" ]; then
  echo "Found correct MySQL JDBC connector [$existingConnector]."
else
  if [ -n "$existingConnector" ]; then
      echo
      echo "Found existing MySQL JDBC connector [$existingConnector]."
      read -p "Do you want to change it? [y/N]" CHANGE_MYSQL
      if [[ "${CHANGE_MYSQL}" =~ [yY] ]]; then
          echo "Removing existing MySQL JDBC connector [$existingConnector]."
          rm ${TOMCAT_LIB}/${existingConnector} && copyOrDownloadMysqlConnector || exit $?
      fi
  else
      echo
      echo "No MySQL JDBC connector found. Download or copy one needed."
      copyOrDownloadMysqlConnector || exit $?
  fi
fi

echo
echo "Configuration completed successfully!"
echo "You can now start up the Artifactory service to use Artifactory with MySQL."
echo "Press enter to exit..."
read
exit 0
