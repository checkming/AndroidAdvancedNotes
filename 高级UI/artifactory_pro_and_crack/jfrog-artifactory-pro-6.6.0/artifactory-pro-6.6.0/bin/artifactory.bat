@echo off
title Artifactory
echo.
echo Starting Artifactory...
echo.
echo To stop, press Ctrl+c

setlocal

rem defaults
set ARTIFACTORY_HOME=%~dp0..
set CATALINA_HOME=%ARTIFACTORY_HOME%\tomcat
set JAVA_OPTIONS=-server -Xms512m -Xmx2g -Xss256k -XX:+UseG1GC
set CATALINA_OPTS=%JAVA_OPTIONS% -Dartifactory.home="%ARTIFACTORY_HOME%" -Dfile.encoding=UTF8 -Djruby.compile.invokedynamic=false -Djruby.bytecode.version=1.8 -Dartdist=zip -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true

if not "%JAVA_HOME%" == "" goto javaVersion
if not "%JRE_HOME%" == "" goto javaVersion
echo Neither the JAVA_HOME nor the JRE_HOME environment variable is defined
echo Will try to guess them from the registry.

:findJDK
FOR /F "usebackq skip=2 tokens=3" %%A IN (`REG QUERY "HKLM\Software\JavaSoft\Java Development Kit" /v CurrentVersion 2^>nul`) DO (set CurVer=%%A)
FOR /F "usebackq skip=2 tokens=3*" %%A IN (`REG QUERY "HKLM\Software\JavaSoft\Java Development Kit\%CurVer%" /v JavaHome 2^>nul`) DO (set JAVA_HOME=%%A %%B)
if not "%JAVA_HOME%" == "" goto javaAvailable

:findJRE
FOR /F "usebackq skip=2 tokens=3" %%A IN (`REG QUERY "HKLM\Software\JavaSoft\Java Runtime Environment" /v CurrentVersion 2^>nul`) DO (set CurVer=%%A)
FOR /F "usebackq skip=2 tokens=3*" %%A IN (`REG QUERY "HKLM\Software\JavaSoft\Java Runtime Environment\%CurVer%" /v JavaHome 2^>nul`) DO (set JRE_HOME=%%A %%B)
goto javaAvailable

:javaVersion
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    rem @echo Output: %%g
    set CurVer=%%g
	rem set CurVer=%CurVer:"=.%
)

:javaAvailable
set t=%CurVer:"=%
for /f "delims=. tokens=1-3" %%v in ("%t%") do (
    rem @echo Major: %%v
    rem @echo Minor: %%w
    rem @echo Build: %%x
	set major=%%v
	set minor=%%w
)

if %major% gtr 1 goto javaOk
if %minor% gtr 7 goto javaOk
echo Java version must be 1.8 or newer, you have %CurVer% configured
goto end

:javaOk
echo java ok
rem start
"%CATALINA_HOME%\bin\catalina.bat" run

@endlocal
:end

