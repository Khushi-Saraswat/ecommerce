package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private int id;

    private String name;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;

    private Boolean defaultAddress;
}
