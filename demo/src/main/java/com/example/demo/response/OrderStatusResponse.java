package com.example.demo.response;

import com.example.demo.constants.OrderStatus;

import lombok.Data;

@Data
public class OrderStatusResponse {

    private OrderStatus status;

}
