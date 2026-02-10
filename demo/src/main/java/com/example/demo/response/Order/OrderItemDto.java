package com.example.demo.response.Order;

import com.example.demo.response.Product.ProductResponseDTO;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private ProductResponseDTO product;
    private Integer quantity;
    private Double price;
    private Double itemTotal;

}
