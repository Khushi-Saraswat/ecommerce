package com.example.demo.exception;

public class UserAlreadyExist extends RuntimeException {

    public String msg;

    public UserAlreadyExist() {

    }

    public UserAlreadyExist(String msg) {
        super(msg);
        this.msg = msg;
    }
}
