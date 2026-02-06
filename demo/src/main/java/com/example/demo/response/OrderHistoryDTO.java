package com.example.demo.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.constants.OrderStatus;

import lombok.Data;

@Data
public class OrderHistoryDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private AddressDTO deliveryAddress;
    private List<ArtisanOrderDTO> artisanOrders;

}
