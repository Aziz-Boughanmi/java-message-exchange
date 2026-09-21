#!/usr/bin/env bash
# Starts both players inside a single JVM (Task 5).
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"
echo "Building project..."
mvn -q package -DskipTests
echo "Running local (same-JVM) mode..."
java -jar target/player-exercise-1.0.0.jar
