package com.example.demo.response;

import lombok.Data;

@Data
public class DailyMetrics {
    private Long totalUsers;
    private Long totalArtisans;
    private Long totalOrders;
    private double totalRevenue;
}
