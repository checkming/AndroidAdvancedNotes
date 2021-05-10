@echo off & cls & echo.

net file 1>NUL 2>NUL & if ERRORLEVEL 1 (echo "You must right-click and select &  ECHO \"RUN AS ADMINISTRATOR\"  to run this batch. Exiting..." & echo. & pause & exit /D)

setlocal

set SERVICE_NAME=Artifactory
set EXECUTABLE=..\bin\artifactory-service.exe

"%EXECUTABLE%" //DS//%SERVICE_NAME%
echo The service '%SERVICE_NAME%' has been removed