package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.OrderAddress;
import com.example.demo.model.Product;
import com.example.demo.model.UserDtls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderDto {
    private String orderId;
    private LocalDate orderDate;
    private Product product;
    private Double price;
    private Integer quantity;
    private UserDtls user;
    private String status;
    private String paymentType;
    private OrderAddress orderAddress;
}
