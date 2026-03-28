package com.example.demo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@lombok.ToString(exclude = "orders")
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String addressName;
    private String addressLandMark;
    private String addressState;
    private String addressPhoneNumber;
    private String addressZipCode;

    private String city;
    private Boolean defaultAddress;

    @OneToMany(mappedBy = "address")
    private List<Order> orders;

}
