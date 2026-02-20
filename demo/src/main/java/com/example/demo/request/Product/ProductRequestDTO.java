package com.example.demo.request.Product;

import lombok.Data;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private Double mrp;
    private Boolean isActive;
    // private CategoryRequestDTO categoryRequestDTO;

    private Long categoryId;
    private Integer stock;

}
