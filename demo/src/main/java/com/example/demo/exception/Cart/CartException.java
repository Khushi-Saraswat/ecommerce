package com.example.demo.exception.Cart;

import com.example.demo.constants.errorTypes.CartErrorType;
import com.example.demo.exception.BaseException;

public class CartException extends BaseException {
    public CartException(String message, CartErrorType type) {
        super(message, "CART_" + type.name());
    }
}
