package com.exercise;

/**
 * entry point of the prog
 *Responsibilities:
 *  read the command-line arguments to decide which mode to run
 *  local mode no args- 2 players in the same JVM
 *  server mode -server ... - 1 player waiting for a connection
 *  client mode -client ... -: 1 player connecting to the server
 * Usage:
 *  java -jar player-communication-1.0.0.jar
 *  java -jar player-communication-1.0.0.jar server 9999 Bob
 *  java -jar player-communication-1.0.0.jar client localhost 9999 Alice
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            runLocal();
        } else if ("server".equals(args[0]) && args.length == 3) {
            runServer(Integer.parseInt(args[1]), args[2]);
        } else if ("client".equals(args[0]) && args.length == 4) {
            runClient(args[1], Integer.parseInt(args[2]), args[3]);
        } else {
            System.out.println("Usage:");
            System.out.println("java -jar player-communication-1.0.0.jar");
            System.out.println("java -jar player-communication-1.0.0.jar server <port> <name>");
            System.out.println("java -jar player-communication-1.0.0.jar client <host> <port> <name>");
        }
    }
// Both players run in the same JVM - Task 5
private static void runLocal() throws InterruptedException {
    // create and cross-connect the communicators
    LocalCommunicator comAlice = new LocalCommunicator();
    LocalCommunicator comBob= new LocalCommunicator();
    comAlice.connect(comBob); // Alice sends  Bob's inbox
    comBob.connect(comAlice);// Bob sends  Alice's inbox
    //create the two players
    Player alice = new Player("Alice", comAlice, true);  // initiator
    Player bob = new Player("Bob", comBob, false); // responder
    System.out.println("local mode- both players in one JVM ");

    //each player runs in its own thread
    Thread aliceThread = new Thread(alice);
    Thread bobThread = new Thread(bob);
    bobThread.start(); // Bob starts first so he is ready to receive
    aliceThread.start();
    // main thread waits here until both are done
    aliceThread.join();
    bobThread.join();
    System.out.println(" Done");
}
// Task 7 - responder side own jvm process
private static void runServer(int port, String playerName) throws Exception {
    SocketCommunicator comm = SocketCommunicator.createServer(port);
    Player responder = new Player(playerName,comm, false);
    responder.run();
}// Task 7 - initiator side own jvm process
private static void runClient(String host, int port,String playerName) throws Exception {
    SocketCommunicator comm = SocketCommunicator.createClient(host, port);
    Player initiator = new Player(playerName,comm, true);
    initiator.run();
}
}