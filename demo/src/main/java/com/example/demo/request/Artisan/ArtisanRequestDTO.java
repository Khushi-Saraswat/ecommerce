package com.example.demo.request.Artisan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtisanRequestDTO {

    private String brandName;
    private String artisianType;
    private String bio;
    private String city;
    private String state;
    private String pincode;

}
