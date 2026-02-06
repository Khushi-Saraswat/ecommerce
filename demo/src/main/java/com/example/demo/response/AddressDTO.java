package com.example.demo.response;

import lombok.Data;

@Data
public class AddressDTO {
    private Integer id;
    private String name;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private Boolean defaultAddress;
}
