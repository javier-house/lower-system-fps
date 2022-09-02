#!/bin/bash
processID=$(ps -ef | grep LowerSystemFps.jar | grep -v 'grep' | awk '{print $2}')
if [ -z "$processID" ]; then
  echo "程序未启动..."
else
  echo "程序正在运行中..."
fi