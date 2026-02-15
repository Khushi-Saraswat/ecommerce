package com.example.demo.request.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
    private String imageUrl;
    private Boolean isPrimary;
}
