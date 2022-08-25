#!/bin/bash
processID=$(ps -ef | grep LowerSystemFps.jar | grep -v 'grep' | awk '{print $2}')
if [ -n "$processID" ]; then
  kill -15 $processID
  echo "程序已停止..."
else
  echo "程序未运行..."
fi