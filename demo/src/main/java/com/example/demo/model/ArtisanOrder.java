package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.constants.ArtisanOrderStatus;
import com.example.demo.constants.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

//1 artisan ka part of order.
@Data
@Entity
@Table(name = "artisanOrder")
public class ArtisanOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "artisan_id")
    private Artisan artisan;

    private BigDecimal subtotal;

    @Enumerated(EnumType.STRING)
    private ArtisanOrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String razorpayOrderId;

    @OneToMany(mappedBy = "artisanOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

}
