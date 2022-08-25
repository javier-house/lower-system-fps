#!/bin/bash
processID=$(ps -ef | grep LowerSystemFps.jar | grep -v 'grep' | awk '{print $2}')
if [ -z "$processID" ]; then
  if [ ! -d "../logs/" ]; then
    mkdir ../logs
  fi
  echo "启动中..."
  nohup java -Xmx64m -jar ../jar/LowerSystemFps.jar >../logs/LowerSystemFps.log 2>&1 &
  echo "启动完毕..."
else
  echo "程序正在运行中..."
fi