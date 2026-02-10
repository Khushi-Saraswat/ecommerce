package com.example.demo.response.Artisan;

import com.example.demo.constants.KycStatus;

import lombok.Data;

@Data
public class ArtisanResponseDTO {

    private String name;
    private String username;
    private String brandName;
    private String artisianType;
    private String bio;
    private String city;
    private String state;
    private String pincode;
    private KycStatus kycStatus;
}
