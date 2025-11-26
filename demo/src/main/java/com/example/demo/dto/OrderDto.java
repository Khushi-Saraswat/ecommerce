package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String PaymentType;
}
