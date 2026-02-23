package com.example.demo.service.methods;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.constants.OrderStatus;
import com.example.demo.request.Order.OrderRequestDTO;
import com.example.demo.response.Order.OrderHistoryDTO;
import com.example.demo.response.Order.OrderResponseDTO;

public interface OrderService {

    public String saveOrder(OrderRequestDTO orderRequestDTO);

    // public List<OrderHistoryDTO> getOrdersByUser(String jwt);

    // by admin - status-shipped.delivered
    public String updatOrderStatus(Long artisanId, String status);

    OrderResponseDTO updateOrderStatusByArtisan(Long orderId, OrderStatus status);
    // public List<Order> getAllOrders();

    // public Order getOrdersByOrderId(Long orderId);
    public OrderResponseDTO getOrderById(Long orderId);

    public Page<OrderHistoryDTO> getOrdersByUser(Pageable pageable);

    public Page<OrderHistoryDTO> getOrdersByArtisan(Pageable pageable);

    public String updateAllOrderStatus(Long orderId, String status);

    // public Page<Order> getAllActiveOrderPagination(Integer pageNo, Integer
    // PageSize);

    public String cancelOrder(Long orderId) throws BadRequestException;

    public Long getOrderCount();

    public String paymentCallback();

    // public List<Order> OrderByStatus(String status);

}
