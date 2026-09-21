package com.exercise;
/**
 * a message sent between two players
 * responsibilities:
 *  - hold the text content of the message.
 *  - hold the name of the player who sent it
 *  - fields are final so the message never changes after creation : immutable
 */
public final class Message {
    private final String content;
    private final String sender;
    public Message(String content, String sender) {
        this.content = content;
        this.sender  = sender;
    }
    public String getContent() { return content; }
    public String getSender() { return sender; }
    @Override
    public String toString() {
        return "[" + sender + "]" + content;
    }
}