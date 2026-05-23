@echo off
setlocal
set MAVEN_VERSION=3.9.9
set BASE_DIR=%~dp0
set MVN_HOME=%BASE_DIR%.mvn\apache-maven-%MAVEN_VERSION%
set MVN_CMD=%MVN_HOME%\bin\mvn.cmd

if not exist "%MVN_CMD%" (
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $base='%BASE_DIR%'; $version='%MAVEN_VERSION%'; $url='https://archive.apache.org/dist/maven/maven-3/' + $version + '/binaries/apache-maven-' + $version + '-bin.zip'; $zip=Join-Path $env:TEMP ('apache-maven-' + $version + '-bin.zip'); $dest=Join-Path $base '.mvn'; New-Item -ItemType Directory -Force -Path $dest | Out-Null; if (!(Test-Path $zip)) { Invoke-WebRequest -Uri $url -OutFile $zip }; Expand-Archive -Path $zip -DestinationPath $dest -Force"
)

call "%MVN_CMD%" %*
set EXIT_CODE=%ERRORLEVEL%
endlocal & exit /b %EXIT_CODE%
