package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String title;
    private String description;
    private String category;
    private Double price;
    private int stock;
    private String image;
    private int discount;
    private Double discountPrice;
    private Boolean isActive;
}
