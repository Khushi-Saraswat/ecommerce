package com.example.demo.response.Others;

import lombok.Data;

@Data
public class DailyMetrics {
    private Long totalUsers;
    private Long totalArtisans;
    private Long totalOrders;
    private double totalRevenue;
}
