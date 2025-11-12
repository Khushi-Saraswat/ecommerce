package com.example.demo.exception;

public class EmailLinkSend extends RuntimeException {

    public String message;

    public EmailLinkSend() {

    }

    public EmailLinkSend(String message) {
        super(message);
        this.message = message;
    }

}
