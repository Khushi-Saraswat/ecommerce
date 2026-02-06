package com.example.demo.response;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private Integer id;
    private String name;
    private Double price;
    private Double mrp;
    private Double rating;
    private String category;
    private Boolean isActive;
    private ArtisanResponseDTO artisan;

}
