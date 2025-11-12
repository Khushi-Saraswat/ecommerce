package com.example.demo.exception;

public class UserNotFoundException extends RuntimeException {

    public String msg;

    public UserNotFoundException() {

    }

    public UserNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }

}
