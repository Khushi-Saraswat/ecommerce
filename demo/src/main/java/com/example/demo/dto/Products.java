package com.example.demo.dto;

import com.example.demo.request.category.CategoryRequestDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Products {

    private String name;
    private String description;
    private Double price;
    private Double mrp;
    private Boolean isActive;
    private CategoryRequestDTO categoryRequestDTO;

    private Integer stock;

}
