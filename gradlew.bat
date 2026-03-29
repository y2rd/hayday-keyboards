@ECHO OFF
SET DIRNAME=%~dp0
SET APP_BASE_NAME=%~n0
SET APP_HOME=%DIRNAME%

SET DEFAULT_JVM_OPTS=

"%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" %*
