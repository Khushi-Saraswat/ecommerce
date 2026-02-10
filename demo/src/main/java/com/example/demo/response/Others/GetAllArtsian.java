package com.example.demo.response.Others;

import com.example.demo.constants.KycStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllArtsian {

    private String name;
    private String email;
    private String brandName;
    private String artisianType;
    private String bio;
    private String pincode;
    private String city;
    private String state;
    private KycStatus kycStatus;

}
