package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderId;

    private LocalDate orderDate;

    private Double price;

    private Integer quantity;

    @ManyToOne
    private UserDtls user;

    private String status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private OrderAddress orderAddress;

    private double delievery;

    private double tax;

}
