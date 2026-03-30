package com.example.demo.response.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.constants.OrderStatus;
import com.example.demo.constants.PaymentStatus;
import com.example.demo.response.Address.AddressResponse;
import com.example.demo.response.Artisan.ArtisanResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long userId;
    private Long orderId; // Note: ModelMapper might need a custom mapping for 'id' to 'orderId'
    private BigDecimal totalAmount;
    
    // Removed the ambiguous 'status' field to fix the error
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    
    private String paymentType;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    private List<ArtisanResponseDTO> artisanOrders;
    private AddressResponse address;
}
