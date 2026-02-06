package com.example.demo.exception.Auth;

import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.exception.BaseException;

public class AuthException extends BaseException {

    private final AuthErrorType type;

    public AuthException(String message, AuthErrorType type) {
        super(message, "AUTH_" + type.name());
        this.type = type;
    }
}
