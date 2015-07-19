#!/bin/bash
GRADLE_PATH="./gradlew"
echo "The_Fireplace's Forge Tools - Setup v1.3"

which gradle && GRADLE_PATH="$(which gradle)"

"$GRADLE_PATH" setupDecompWorkspace
"$GRADLE_PATH" eclipse
echo "****************************"
echo "Forge eclipse workspace setup completed!"
echo "****************************"
echo "Press any key to continue..."
read -n 1 c