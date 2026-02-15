package com.example.demo.response.Others;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProduct {

    private String name;
    private String description;
    private Double price;
    private Double mrp;
    private Boolean isActive;
    // private Category category;

    private Integer stock;

    private List<String> imageUrls;

}
