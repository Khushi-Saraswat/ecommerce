package com.example.demo.response.Order;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.constants.ArtisanOrderStatus;
import com.example.demo.response.Artisan.ArtisanResponseDTO;

import lombok.Data;

@Data
public class ArtisanOrderDTO {
    private Long id;
    private ArtisanResponseDTO artisan;
    private BigDecimal subtotal;
    private ArtisanOrderStatus status;
    private List<OrderItemDto> items;

}
