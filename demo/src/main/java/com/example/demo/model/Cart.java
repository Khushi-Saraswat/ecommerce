package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//keep track of items added to the cart before checkout
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // each cart item belongs to one user
    @ManyToOne
    private UserDtls user;
    // each cart item points to 1 product
    @ManyToOne
    private Product product;
    private Integer quantity;

    // totalPrice and totalOrderPrice are not stored in database.used for runtime
    // calculations.
    @Transient
    private Double totalPrice;
    @Transient
    private Double totalOrderPrice;

}
