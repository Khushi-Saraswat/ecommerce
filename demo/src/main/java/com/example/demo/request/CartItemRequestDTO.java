package com.example.demo.request;

import lombok.Data;

@Data
public class CartItemRequestDTO {
    private Integer productId;
    private Integer quantity;

}
