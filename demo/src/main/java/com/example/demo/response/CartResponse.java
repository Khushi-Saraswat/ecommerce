package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private ProductResponseDTO product;
    private Integer quantity;
    private Double totalPrice;
    private String ProductName;
    private Double ItemTotal;
    private Double TotalOrderPrice;

    // private Integer cartTotal;
}
