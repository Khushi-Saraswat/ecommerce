package com.example.demo.dto;

import com.example.demo.model.Product;
import com.example.demo.model.UserDtls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private UserDtls user;
    private Product product;
    private Integer quantity;
}
