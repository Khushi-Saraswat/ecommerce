package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.response.Artisan.ArtisanResponseDTO;
import com.example.demo.response.Order.OrderResponseDTO;
import com.example.demo.response.Others.DailyMetrics;
import com.example.demo.response.User.UserResponseDTO;

public interface AdminService {

    List<ArtisanResponseDTO> getAllArtisans();

    String approveArtisan(Long artisanId);

    String rejectArtisan(Long artisanId);

    List<UserResponseDTO> getAllUsers();

    String blockUser(Long userId);

    List<OrderResponseDTO> getAllOrders();

    DailyMetrics getDailyMetrics();

}
