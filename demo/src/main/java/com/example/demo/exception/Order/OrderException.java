package com.example.demo.exception.Order;

import com.example.demo.constants.errorTypes.OrderErrorType;
import com.example.demo.exception.BaseException;

public class OrderException extends BaseException {
    public OrderException(String message, OrderErrorType type) {
        super(message, "ORDER_" + type.name());
    }
}
