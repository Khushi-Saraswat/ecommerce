package com.example.demo.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.constants.OrderStatus;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<ArtisanResponseDTO> artisanOrders;
    private AddressResponse address;

}
