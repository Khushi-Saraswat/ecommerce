package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.response.Order.OrderHistoryDTO;

public interface OrderService {

    public String saveOrder(String jwt, Integer addressId, String PaymentType);

    // public List<OrderHistoryDTO> getOrdersByUser(String jwt);

    // by artisan - status-shipped.delivered
    public String updatOrderStatus(String jwt, Integer artisanId, String status);

    // public List<Order> getAllOrders();

    // public Order getOrdersByOrderId(Long orderId);

    public List<OrderHistoryDTO> getOrdersByUser(String jwt);

    public List<OrderHistoryDTO> getOrdersByArtisan(String jwt);

    // public Page<Order> getAllActiveOrderPagination(Integer pageNo, Integer
    // PageSize);

    public String cancelOrder(String orderId);

    public Long getOrderCount();

    public String paymentCallback();

    // public List<Order> OrderByStatus(String status);

}
