@ECHO OFF
TITLE The_Fireplace's Forge Tools - Setup v1.4
call gradlew setupDecompWorkspace
call gradlew eclipse
call gradlew idea
ECHO ****************************
ECHO Forge eclipse and idea workspaces setup successfully!
ECHO ****************************
PAUSE