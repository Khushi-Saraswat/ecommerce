package com.example.demo.exception;

public class CategoryExist extends RuntimeException {

    public String message;

    public CategoryExist() {

    }

    public CategoryExist(String message) {
        super(message);
    }

}
