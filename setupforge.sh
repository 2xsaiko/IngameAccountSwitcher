#!/bin/bash
GRADLE_PATH="./gradlew"
echo "The_Fireplace's Forge Tools - Setup v1.4"

which gradle && GRADLE_PATH="$(which gradle)"

"$GRADLE_PATH" setupDecompWorkspace
"$GRADLE_PATH" eclipse
"$GRADLE_PATH" idea
echo "****************************"
echo "Forge eclipse and idea workspaces setup successfully!"
echo "****************************"
echo "Press any key to continue..."
read -n 1 c