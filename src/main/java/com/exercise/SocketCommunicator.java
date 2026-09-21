package com.exercise;
import java.io.*;
import java.net.*;

/**
 * cross-process transport between two players using a TCP socket.
 * Responsibilities:
 *  - let two players in separate JVM processes exchange messages over the network.
 *  - send a message as a plain text line: "senderName|content"
 *  - receive a line and parse it back into a Message.
 *  - handle the STOP_SIGNAL so the responder process exits cleanly.
 *  two static factory methods hide the socket setup:
 *  - createServer(port)        : waits for the other player to connect
 *  - createClient(host, port)  : connects to the other player
 */
public class SocketCommunicator implements Communicator {
    private final Socket socket;
    private final ServerSocket serverSocket; // only used on the server side
    private final BufferedReader in;
    private final PrintWriter out;
    // private constructor - use createServer() or createClient() instead
    private SocketCommunicator(Socket socket, ServerSocket serverSocket) throws IOException {
        this.socket  = socket;
        this.serverSocket = serverSocket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true); // true = auto flash
    }
    // server side- open a port and wait for the other player to connect.
    public static SocketCommunicator createServer(int port) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Waiting for connection on port " + port + "...");
        Socket client = ss.accept(); // blocks until client connects
        System.out.println("Player connected.");
        return new SocketCommunicator(client, ss);
    }
    // client side- connect to the other player who is already listening.
    public static SocketCommunicator createClient(String host, int port) throws IOException {
        Socket s = new Socket(host, port);
        System.out.println("Connected to " + host + ":" + port);
        return new SocketCommunicator(s, null);
    }
    // Send- write the message as one text line - format: "sendername-content"
    @Override
    public void send(Message message) {
        if (Communicator.STOP_SIGNAL.equals(message.getContent())) {
            out.println(Communicator.STOP_SIGNAL);
        } else {out.println(message.getSender() + "|" + message.getContent());
        }
    }
    // receive- read one line and parse it back into a Message.
    @Override
    public Message receive() throws IOException {
        String line = in.readLine(); // blocks until a line arrives
        if (line == null || Communicator.STOP_SIGNAL.equals(line)) {
            return new Message(Communicator.STOP_SIGNAL, "");
        }
        int separatorIndex = line.indexOf('|');
        String sender = line.substring(0, separatorIndex);
        String content = line.substring(separatorIndex + 1);
        return new Message(content, sender);
    }
    // close the socket and ServerSocket if we are the server side.
    @Override
    public void close() throws IOException {
        socket.close();
        if (serverSocket != null) {serverSocket.close();}
    }
}