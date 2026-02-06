package com.example.demo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResponse {
    

     private String productId;
     private Boolean available;
     private Integer stock;
     private String status;



}
