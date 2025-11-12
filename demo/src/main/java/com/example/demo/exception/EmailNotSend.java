package com.example.demo.exception;

public class EmailNotSend extends RuntimeException {

    public String message;

    public EmailNotSend() {

    }

    public EmailNotSend(String message) {
        super(message);
        this.message = message;
    }
}
