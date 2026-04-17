package com.example.demo.response.Product;

import java.io.Serializable;

import lombok.Data;

@Data
public class DeleteProductResponseDTO implements Serializable{
    private boolean success;
    private String message;
    private Integer productId;

}
