package com.example.demo.response.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.constants.OrderStatus;
import com.example.demo.response.Address.AddressResponse;
import com.example.demo.response.Artisan.ArtisanResponseDTO;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<ArtisanResponseDTO> artisanOrders;
    private AddressResponse address;
    private String paymentType;
    private String paymentStatus;

}
