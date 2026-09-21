# Java Message Exchange

A pure Java messaging system demonstrating both in-process and inter-process communication using a clean and minimal design.

## Overview
This project implements a simple message exchange between two `Player` instances.
One player acts as the initiator and starts the communication.
Whenever a player receives a message, it responds with a new message containing:
- the received message
- its own sent-message counter
The program stops gracefully after the initiator has sent 10 messages and received 10 responses.

The same behavior is implemented in two different execution modes:
1. Both players running in the same Java process
2. Each player running in a separate Java process

## Requirements Implemented
- Two communicating players
- Initiator starts the exchange
- Message counter included in responses
- Graceful termination after 10 message exchanges
- Same-process communication
- Separate-process communication
- Pure Java implementation
- Maven project structure
- Shell scripts for execution
- Separation between communication logic and player behavior

## Architecture
The project separates the player behavior from the communication mechanism.

### Main Components
- `Player`  
  Contains the behavior of a player and manages message counters.
- `Message`  
  Represents the messages exchanged between players.
- `Communicator`  
  Defines the communication abstraction used by players.
- `LocalCommunicator`  
  Handles communication when both players run inside the same JVM.
- `SocketCommunicator`  
  Handles communication between players running in separate JVM processes.
- `Main`  
  Application entry point and configuration.
This design allows the player logic to remain independent from the underlying communication mechanism.

## Communication Modes
### Local Mode
Both players run inside the same Java process and communicate through `LocalCommunicator`.

Run with:
```bash
./start-local.sh
```
### Distributed Mode
Each player runs in a separate Java process and communicates using TCP sockets.
Run with:
```bash
./start-distributed.sh
```
## Project Structure
```text
java-message-exchange/
├── pom.xml
├── start-local.sh
├── start-distributed.sh
└── src/
    └── main/
        └── java/
            └── com/
                └── exercise/
                    ├── Communicator.java
                    ├── LocalCommunicator.java
                    ├── Main.java
                    ├── Message.java
                    ├── Player.java
                    └── SocketCommunicator.java
```
## Technologies
- Java
- Maven
- TCP sockets
- Shell scripting
- Git
## Design Goals
The implementation focuses on:
- simplicity
- separation of concerns
- clean object-oriented design
- minimal dependencies
- reuse of the same player logic across different communication modes
