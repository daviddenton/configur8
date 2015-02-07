#!/bin/bash
echo Releasing and publishing v$1
./sbt "set version:=\"$1\"" clean compile test +package +publish