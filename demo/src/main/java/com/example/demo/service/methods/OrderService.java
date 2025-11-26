package com.example.demo.service.methods;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.dto.OrderDto;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;

public interface OrderService {

    public void saveOrder(UserDtls userDtls, OrderDto orderDto) throws Exception;

    public List<ProductOrder> getOrdersByUser(Long userId);

    public ProductOrder updatOrderStatus(Integer id, String st);

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrdersByOrderId(String orderId);

    public Page<ProductOrder> getAllActiveOrderPagination(Integer pageNo, Integer PageSize);
}
