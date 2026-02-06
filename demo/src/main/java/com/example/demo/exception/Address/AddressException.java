package com.example.demo.exception.Address;

import com.example.demo.constants.errorTypes.AddressErrorType;
import com.example.demo.exception.BaseException;

public class AddressException extends BaseException {
    public AddressException(String message, AddressErrorType type) {
        super(message, type.getCode());
    }
}
