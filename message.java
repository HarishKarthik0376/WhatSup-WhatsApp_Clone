package com.example.whatsup.Models;

public class message {
    String message,messageid,uid;
    Long timestamp;

    public message(String message, String uid, Long timestamp) {
        this.message = message;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public message(String message, String uid) {
        this.message = message;
        this.uid = uid;
    }

    public message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
