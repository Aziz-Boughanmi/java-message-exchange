#!/usr/bin/env bash
# Starts each player in its own JVM process (Task 7).
# Process A (Bob, responder) is launched in the background first;
# Process B (Alice, initiator) then connects and drives the exchange.
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"
PORT=9999
JAR="target/player-exercise-1.0.0.jar"
echo "Building project..."
mvn -q package -DskipTests
echo ""
echo "Starting Bob (responder) on port $PORT in background..."
java -jar "$JAR" server "$PORT" Bob &
SERVER_PID=$!
# Give the server a moment to open the listening socket before the client connects.
sleep 1
echo "Starting Alice (initiator) — connecting to localhost:$PORT..."
java -jar "$JAR" client localhost "$PORT" Alice
# Wait for the responder process to finish cleanly.
wait "$SERVER_PID"
echo ""
echo "Both processes finished."
