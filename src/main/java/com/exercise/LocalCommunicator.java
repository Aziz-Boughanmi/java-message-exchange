package com.exercise;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * same-JVM transport between two players
 * responsibilities:
 *  - own an inbox queue where incoming messages are stored
 *  - send a message by putting it directly into the target's inbox
 *  - receive a message by taking it from its own inbox - blocks until one arrives
 * how to wire two players together:
 *  localCommunicator commA = new LocalCommunicator();
 *  localCommunicator commB = new LocalCommunicator();
 *  commA.connect(commB);  a sends to b
 *  commB.connect(commA); b sends to a
 */
public class LocalCommunicator implements Communicator {
    // inbox is the queue where messages for this player arrive
    private final BlockingQueue<Message> inbox = new LinkedBlockingQueue<>();
    // target is the other player's communicator
    private LocalCommunicator target;
    // call this after creating both communicators to link them together
    public void connect(LocalCommunicator target) {
        this.target = target;
    }
    // send - put the message into the target's inbox
    @Override
    public void send(Message message) throws InterruptedException {target.inbox.put(message);
    }
    // receive - take the next message from our own inbox - blocks if empty
    @Override
    public Message receive() throws InterruptedException {return inbox.take();
    }
    // nothing to close for an in-memory queue
    @Override
    public void close() {}
}