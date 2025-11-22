package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // one user can have multiple wishlist items.
    @ManyToOne
    private UserDtls user;
    // one product can have multiple wishlist
    @ManyToOne
    public Product product;
    // used to calculate total items in runtime
    @Transient
    private Integer TotalProduct;
}
