package com.example.demo.model;

public class Message {

    private String message;
    private String sendId;
    private String sendName;
    private String time;

    public Message(String message, String sendId, String sendName, String time) {
        this.message = message;
        this.sendId = sendId;
        this.sendName = sendName;
        this.time = time;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
