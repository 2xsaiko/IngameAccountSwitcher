@ECHO OFF
TITLE The_Fireplace's Forge Tools - Setup v1.2
call gradlew setupDecompWorkspace
call gradlew eclipse
ECHO ****************************
ECHO Forge workspace setup completed!
ECHO ****************************
PAUSE