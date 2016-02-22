#!/bin/bash


echo "============="
echo "ByteMe - LIMA"
echo "============="

echo "Updating local repo"
git pull

echo "Executing npm install"
npm install

echo "Executing npm run build"
npm run build

echo "Bundling and Packaging"
mvn clean package

echo "====="
echo " END "
echo "====="

echo "Running LIMA"
java -jar target/lima-0.0.1-SNAPSHOT.jar
