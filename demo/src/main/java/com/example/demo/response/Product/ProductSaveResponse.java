package com.example.demo.response.Product;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveResponse  implements Serializable{

    private int artisanId;
    private int productId;
    private String configurationMessage;
    private boolean success;
}
