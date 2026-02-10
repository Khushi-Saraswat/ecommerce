package com.example.demo.request.Order;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private Long addressId;
    private String paymentType;
}
