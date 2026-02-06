package com.example.demo.request;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private Long addressId;
    private String paymentType;
}
