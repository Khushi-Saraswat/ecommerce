package com.example.demo.exception;

public class InvalidEmail extends RuntimeException {

    public String message;

    public InvalidEmail() {

    }

    public InvalidEmail(String message) {
        super(message);
        this.message = message;
    }
}
