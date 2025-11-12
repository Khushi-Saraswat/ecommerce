package com.example.demo.exception;

public class AuthenticationIsNotValid extends RuntimeException {

    public String message;

    public AuthenticationIsNotValid() {

    }

    public AuthenticationIsNotValid(String message) {
        super(message);
        this.message = message;
    }
}
