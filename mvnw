#!/bin/sh

MAVEN_PROJECTBASEDIR=$(cd "$(dirname "$0")"; pwd -P)
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

DIST_URL=$(grep -E "^distributionUrl=" "$WRAPPER_PROPERTIES" | sed -e 's/distributionUrl=//')
WRAPPER_URL=$(grep -E "^wrapperUrl=" "$WRAPPER_PROPERTIES" | sed -e 's/wrapperUrl=//')

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Downloading Maven Wrapper JAR from $WRAPPER_URL"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR" || { echo "Failed to download Maven Wrapper JAR"; exit 1; }
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR" || { echo "Failed to download Maven Wrapper JAR"; exit 1; }
  else
    echo "Please install curl or wget to download Maven Wrapper JAR"; exit 1;
  fi
fi

JAVA_EXE="java"
if [ -n "$JAVA_HOME" ]; then
  JAVA_EXE="$JAVA_HOME/bin/java"
fi

exec "$JAVA_EXE" -classpath "$WRAPPER_JAR" -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" org.apache.maven.wrapper.MavenWrapperMain "$@"
