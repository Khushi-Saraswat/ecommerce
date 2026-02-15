package com.example.demo.request.Product;

import com.example.demo.request.category.CategoryRequestDTO;

import lombok.Data;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private Double mrp;
    private Boolean isActive;
    private CategoryRequestDTO categoryRequestDTO;

    private String categoryId;
    private Integer stock;

}
