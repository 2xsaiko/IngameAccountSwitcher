@ECHO OFF
TITLE The_Fireplace's Forge Tools - Full Build v1.0
call gradlew build --refresh-dependencies eclipse
ECHO ****************************
ECHO Building mod completed!
ECHO ****************************
PAUSE