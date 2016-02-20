@echo off

echo =============
echo ByteMe - LIMA
echo =============

echo Executing npm install
call npm install

echo Executing npm run build
call npm run build

echo Bundling and Packaging
call mvn clean package -DskipTests

echo =====
echo  END 
echo =====

pause