package com.example.demo.response.Product;

import java.io.Serializable;

import com.example.demo.request.Product.ProductImageDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO  implements Serializable{
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
