#!/bin/sh
#
# Gradle 启动脚本
#

# 尝试设置 APP_HOME
PRG="$0"
while [ -h "$PRG" ]; do
    ls=$(ls -ld "$PRG")
    link=$(expr "$ls" : '.*-> \(.*\)$')
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=$(dirname "$PRG")/"$link"
    fi
done
APP_HOME=$(cd "$(dirname "$PRG")" && pwd -P)
APP_BASE_NAME=$(basename "$0")

# 使用最大兼容性设置
export GRADLE_OPTS="-Dfile.encoding=UTF-8"

# 启动 Gradle
exec java $GRADLE_OPTS -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
