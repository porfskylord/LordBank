@echo off
setlocal

set SRC_DIR=src
set BIN_DIR=bin
set JAR_FILE=LordBank.jar

:: Create bin directory if it doesn't exist
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

:: Compile all Java files
echo Compiling Java files...
javac -d "%BIN_DIR%" -cp "%BIN_DIR%" "%SRC_DIR%"\**\*.java

:: Create JAR file
echo Creating JAR file...
jar cvfm "%JAR_FILE%" MANIFEST.MF -C "%BIN_DIR%" .

echo Done! Created %JAR_FILE%
