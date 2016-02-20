#!/bin/bash


echo "============="
echo "ByteMe - LIMA"
echo "============="

echo "Executing npm install"
npm install

echo "Executing npm run build"
npm run build

echo "Bundling and Packaging"
mvn clean package

echo "====="
echo " END "
echo "====="
