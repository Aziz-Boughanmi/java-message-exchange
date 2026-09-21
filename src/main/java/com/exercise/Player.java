package com.exercise;
/**
 * one participant in the conversation.
 * responsibilities:
 *  - keep the player's name and count how many messages it sent
 *  - if initiator: send the first message, reply to each response, stop after 10 rounds
 *  - if responder: wait for messages, reply to each one, stop when STOP_SIGNAL arrives
 *  - implements Runnable so it can run in its own thread
 */
public class Player implements Runnable {
    private static final int maxmess = 10;
    private final String name;
    private final Communicator communicator;
    private final boolean isInitiator;
    private int sentCount = 0;
    public Player(String name, Communicator communicator, boolean isInitiator) {
        this.name = name;
        this.communicator = communicator;
        this.isInitiator= isInitiator;
    }
    public String getName() {return name;}
    @Override
    public void run() {
        try {if (isInitiator) {runAsInitiator();} else {runAsResponder();}
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + ": interrupt");
        } catch (Exception e) {
            System.out.println(name + ": error - " + e.getMessage());
        }
    }
    private void runAsInitiator() throws Exception {
        sentCount++;
        communicator.send(new Message("hello", name));
        System.out.println(name + " sent: Hello");
        int receivedCount = 0;
        while (receivedCount < maxmess) {
            Message reply = communicator.receive();
            receivedCount++;
            System.out.println(name + " received: " + reply);
            if (sentCount < maxmess) {
                sentCount++;
                String replyContent = reply.getContent() + " " + sentCount;
                communicator.send(new Message(replyContent, name));
                System.out.println(name + " sent: " + replyContent);
            }
        }
        communicator.send(new Message(Communicator.STOP_SIGNAL, name));
        System.out.println(name + ":done. sent " + sentCount + " messages");
        communicator.close();
    }
    private void runAsResponder() throws Exception {
        while (true) {
            Message received = communicator.receive();
            if (Communicator.STOP_SIGNAL.equals(received.getContent())) {
                System.out.println(name + ":stop signal recieved.sent " + sentCount + " messages");
                communicator.close();
                break;}
            System.out.println(name + " received:" + received);
            sentCount++;
            String replyContent = received.getContent() + " " + sentCount;
            communicator.send(new Message(replyContent, name));
            System.out.println(name + " sent:" + replyContent);
        }
    }
    
}