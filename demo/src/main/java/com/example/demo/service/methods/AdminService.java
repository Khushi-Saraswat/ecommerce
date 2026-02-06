package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.response.ArtisanResponseDTO;
import com.example.demo.response.DailyMetrics;
import com.example.demo.response.OrderResponseDTO;
import com.example.demo.response.UserResponseDTO;

public interface AdminService {

    List<ArtisanResponseDTO> getAllArtisans(String jwt);

    String approveArtisan(Long artisanId, String jwt);

    String rejectArtisan(Long artisanId, String jwt);

    List<UserResponseDTO> getAllUsers(String jwt);

    String blockUser(Long userId, String jwt);

    List<OrderResponseDTO> getAllOrders(String jwt);

    DailyMetrics getDailyMetrics(String jwt);

}
