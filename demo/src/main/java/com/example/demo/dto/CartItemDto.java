package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CartItemDto {
    private Long productId;
    private String productName;
    private String imageUrl;

    private String skuAttributes;
    private Integer quantity;

    private BigDecimal unitPrice;     // snapshot price
    private BigDecimal currentPrice;  // latest product price

    private Boolean priceChanged;

}
