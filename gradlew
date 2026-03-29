#!/usr/bin/env sh
DIRNAME=`dirname "$0"`
APP_HOME=`cd "$DIRNAME"; pwd`

"$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
