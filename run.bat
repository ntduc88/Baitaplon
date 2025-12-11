@echo off
REM Simple batch file to compile, package and run the app
cd /d "%~dp0"
if not exist out mkdir out
echo Compiling Java sources...
javac -d out src\*.java
if errorlevel 1 (
  echo Compilation failed.
  pause
  exit /b 1
)
echo Creating manifest...
echo Main-Class: App>manifest.txt
echo Creating JAR...
jar cfm quanlydiem.jar manifest.txt -C out .
if errorlevel 1 (
  echo JAR creation failed.
  pause
  exit /b 1
)
echo Running application...
java -jar quanlydiem.jar
pause
