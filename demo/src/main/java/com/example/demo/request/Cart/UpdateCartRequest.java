package com.example.demo.request.Cart;

import lombok.Data;

@Data
public class UpdateCartRequest {
    private Integer productId;
    private Integer quantity;
}
