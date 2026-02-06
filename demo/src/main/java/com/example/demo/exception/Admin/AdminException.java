package com.example.demo.exception.Admin;

import com.example.demo.constants.errorTypes.AdminErrorType;
import com.example.demo.exception.BaseException;

public class AdminException extends BaseException {
    public AdminException(String message, AdminErrorType type) {
        super(message, "ADMIN_" + type.name());
    }
}
