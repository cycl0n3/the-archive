#!/bin/sh

java -Xmx512M -Xdock:name=DiskUsageAnalyzer -jar lib/dua.jar "$@"
