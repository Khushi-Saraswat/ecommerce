package com.example.demo.response;

import java.util.List;

import com.example.demo.model.Category;
import com.example.demo.model.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCat {

    private List<Product> product;
    private List<Category> categorie;

    public ProductCat(List<Product> product, List<Category> categorie) {
        this.product = product;
        this.categorie = categorie;
    }
}
