package com.example.demo.request;

import lombok.Data;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private Double mrp;
    private Integer stock;
    private String category;

}
