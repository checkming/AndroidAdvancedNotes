@echo off & cls & echo.
rem Licensed to the Apache Software Foundation (ASF) under one or more
rem contributor license agreements.  See the NOTICE file distributed with
rem this work for additional information regarding copyright ownership.
rem The ASF licenses this file to You under the Apache License, Version 2.0
rem (the "License"); you may not use this file except in compliance with
rem the License.  You may obtain a copy of the License at
rem
rem     http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.

net file 1>NUL 2>NUL & if ERRORLEVEL 1 (echo "You must right-click and select &  ECHO \"RUN AS ADMINISTRATOR\"  to run this batch. Exiting..." & echo. & pause & exit /D)

if "%OS%" == "Windows_NT" setlocal
set "CURRENT_DIR=%cd%"
cd ..
set ARTIFACTORY_HOME=%cd%
set CATALINA_HOME=%cd%\tomcat
set CATALINA_BASE=%CATALINA_HOME%

set "EXECUTABLE=%ARTIFACTORY_HOME%\bin\artifactory-service.exe"
set SERVICE_NAME=Artifactory
set PR_DISPLAYNAME=Artifactory
set PR_DESCRIPTION=Artifactory Binary Repository
set STARTUP_TYPE=auto

set "JOPTS=-Xms512m;-Xmx2g;-Xss256k;-XX:+UseG1GC"
set "PR_LOGPATH=%ARTIFACTORY_HOME%\logs"
set "PR_CLASSPATH=%CATALINA_HOME%\bin\bootstrap.jar;%CATALINA_HOME%\bin\tomcat-juli.jar"
set PR_STDOUTPUT=auto
set PR_STDERROR=auto

rem Make sure prerequisite environment variables are set
if not "%JAVA_HOME%" == "" goto gotJdkHome
if not "%JRE_HOME%" == "" goto gotJreHome
echo Neither the JAVA_HOME nor the JRE_HOME environment variable is defined
echo Service will try to guess them from the registry.
goto okJavaHome
:gotJreHome
if not exist "%JRE_HOME%\bin\java.exe" goto noJavaHome
if not exist "%JRE_HOME%\bin\javaw.exe" goto noJavaHome
goto okJavaHome
:gotJdkHome
if not exist "%JAVA_HOME%\jre\bin\java.exe" goto noJavaHome
if not exist "%JAVA_HOME%\jre\bin\javaw.exe" goto noJavaHome
if not exist "%JAVA_HOME%\bin\javac.exe" goto noJavaHome
if not "%JRE_HOME%" == "" goto okJavaHome
set "JRE_HOME=%JAVA_HOME%\jre"
goto okJavaHome
:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
echo NB: JAVA_HOME should point to a JDK not a JRE
goto end
:okJavaHome
rem Set the server jvm from JAVA_HOME
set "PR_JVM=%JRE_HOME%\bin\server\jvm.dll"
if exist "%PR_JVM%" goto foundJvm
rem Set the client jvm from JAVA_HOME
set "PR_JVM=%JRE_HOME%\bin\client\jvm.dll"
if exist "%PR_JVM%" goto foundJvm
set PR_JVM=auto
:foundJvm

:doInstall
rem Install the service
echo Installing the service '%SERVICE_NAME%' ...
echo Using CATALINA_HOME:    "%CATALINA_HOME%"
echo Using CATALINA_BASE:    "%CATALINA_BASE%"
echo Using JAVA_HOME:        "%JAVA_HOME%"
echo Using JRE_HOME:         "%JRE_HOME%"
echo Using JVM:              "%PR_JVM%"

"%EXECUTABLE%" //IS//%SERVICE_NAME% --StartClass org.apache.catalina.startup.Bootstrap --StopClass org.apache.catalina.startup.Bootstrap --StartParams start --StopParams stop
if not errorlevel 1 goto installed
echo Failed installing '%SERVICE_NAME%' service
goto end

:installed
"%EXECUTABLE%" //US//%SERVICE_NAME% --JvmOptions "-Dcatalina.base=%CATALINA_BASE%;-Dcatalina.home=%CATALINA_HOME%;-Djava.endorsed.dirs=%CATALINA_HOME%\endorsed" --StartMode jvm --StopMode jvm
"%EXECUTABLE%" //US//%SERVICE_NAME% ++JvmOptions "-Djava.io.tmpdir=%CATALINA_BASE%\temp;-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager;-Djava.util.logging.config.file=%CATALINA_BASE%\conf\logging.properties"
"%EXECUTABLE%" //US//%SERVICE_NAME% ++JvmOptions "-Djruby.bytecode.version=1.8 -Djruby.compile.invokedynamic=false"
"%EXECUTABLE%" //US//%SERVICE_NAME% ++JvmOptions "-Dfile.encoding=UTF8"
"%EXECUTABLE%" //US//%SERVICE_NAME% ++JvmOptions "-Dartdist=zip"
"%EXECUTABLE%" //US//%SERVICE_NAME% ++JvmOptions "-Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true"
"%EXECUTABLE%" //US//%SERVICE_NAME% ++JvmOptions "-Dartifactory.home=%ARTIFACTORY_HOME%" --Startup %STARTUP_TYPE% --LogPrefix artifactory-service
"%EXECUTABLE%" //US//%SERVICE_NAME% ++JvmOptions %JOPTS%
rem --JvmMs 128 --JvmMx 256

echo The service '%SERVICE_NAME%' has been installed.
pause

:end
cd "%CURRENT_DIR%"
