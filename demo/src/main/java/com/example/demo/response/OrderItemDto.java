package com.example.demo.response;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private ProductResponseDTO product;
    private Integer quantity;
    private Double price;
    private Double itemTotal;

}
