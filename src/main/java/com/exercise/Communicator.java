package com.exercise;
/**
 * the transport contract between two players.
 * responsibilities:
 *  - define how messages are sent and received, without caring about the details
 *  - let Player work the same way whether the transport is a queue or a socket
 *  - hold the STOP_SIGNAL constant so all classes share the same value
 * 2 implementation exist:
 *  - LocalCommunicator :same JVM,uses a blockingQueue
 *  - SocketCommunicator : separate processes, uses a tcp socket
 */
public interface Communicator {
    // the poison pill - when a player receives this, it stops
    String STOP_SIGNAL = "__STOP__";
    // send a msg to the other player
    void send(Message message) throws Exception;
    // wait for the next message and return it.blocks until one arrives
    Message receive() throws Exception;
    // clean resources (close queue,socket, streams)
    void close() throws Exception;
}