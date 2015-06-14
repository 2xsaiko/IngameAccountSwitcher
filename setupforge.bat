@ECHO OFF
TITLE The_Fireplace's Forge Tools - Setup v1.3
call gradlew setupDecompWorkspace
call gradlew eclipse
ECHO ****************************
ECHO Forge eclipse workspace setup completed!
ECHO ****************************
PAUSE