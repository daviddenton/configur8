#!/bin/bash
echo Releasing and publishing v$1

git tag -a $1 -m "released version $1 to bintray"
git tag
git push origin $1

cd java
./publish.sh $1

cd ..

cd kotlin
./publish.sh $1

cd ..

cd scala
./publish.sh $1

cd ..

cd javascript

./publish.sh $1

