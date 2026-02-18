package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.response.Artisan.ArtisanResponseDTO;
import com.example.demo.response.Order.OrderResponseDTO;
import com.example.demo.response.Others.DailyMetrics;
import com.example.demo.response.User.UserResponseDTO;

public interface AdminService {

    List<ArtisanResponseDTO> getAllArtisans(String jwt);

    String approveArtisan(Long artisanId);

    String rejectArtisan(Long artisanId);

    List<UserResponseDTO> getAllUsers(String jwt);

    String blockUser(Long userId, String jwt);

    List<OrderResponseDTO> getAllOrders(String jwt);

    DailyMetrics getDailyMetrics(String jwt);

}
