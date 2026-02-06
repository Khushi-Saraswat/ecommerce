package com.example.demo.dto;

import java.util.Map;

public class AddToCartRequestDto {
    private Long productId;
    private Integer quantity;

    // Optional: size, color, variant
    private Map<String, String> attributes;
}
