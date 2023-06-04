package com.example.sentimentanalysis;

public class ChatMessage {
    public static final int SENDER_LEFT = 0;
    public static final int SENDER_RIGHT = 1;

    private String text;
    private int sender;

    public ChatMessage(String text, int sender) {
        this.text = text;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public int getSender() {
        return sender;
    }
}

