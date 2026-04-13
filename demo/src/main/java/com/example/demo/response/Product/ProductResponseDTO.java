package com.example.demo.response.Product;

import com.example.demo.request.Product.ProductImageDTO;
import com.example.demo.response.Artisan.ArtisanResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Integer id;
    private String name;
    private Double price;
    private Double mrp;
    private Double rating;
    private String category;
    private Boolean isActive;
    //private ArtisanResponseDTO artisan;
    private ProductImageDTO productImageDTO;

}
