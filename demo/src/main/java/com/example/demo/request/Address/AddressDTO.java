package com.example.demo.request.Address;

import lombok.Data;

@Data
public class AddressDTO {
    private String name;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private Boolean defaultAddress;

}
