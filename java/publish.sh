#!/bin/bash
echo Releasing and publishing v$1

./gradlew clean build bintrayUpload