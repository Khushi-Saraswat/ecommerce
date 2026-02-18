package com.example.demo.response.Product;

import lombok.Data;

@Data
public class DeleteProductResponseDTO {
    private boolean success;
    private String message;
    private Integer productId;

}
