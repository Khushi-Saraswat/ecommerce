package com.example.demo.exception.User;

import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.BaseException;

public class UserException extends BaseException {

    private final UserErrorType type;

    public UserException(String message, UserErrorType type) {
        super(message, "USER_" + type.name());
        this.type = type;
    }

    public UserErrorType getType() {
        return type;
    }
}
