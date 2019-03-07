#!/bin/bash
echo Releasing and publishing v$1

yarn publish --new-version $1
