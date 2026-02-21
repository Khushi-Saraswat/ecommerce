package com.example.demo.request.Order;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private Integer addressId;
    private String paymentType;
}
