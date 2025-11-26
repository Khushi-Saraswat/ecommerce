package com.example.demo.model;

import com.example.demo.constants.OrderStatus;
import com.example.demo.constants.PaymentType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String mobileNo;

    private String address;

    private String city;

    private String state;

    private String pincode;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
