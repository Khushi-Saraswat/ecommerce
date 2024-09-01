package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.model.OrderRequest;
import com.example.demo.model.ProductOrder;

public interface OrderService {

    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception;

    public List<ProductOrder> getOrdersByUser(Integer userId);

    public ProductOrder updatOrderStatus(Integer id, String st);

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrdersByOrderId(String orderId);

    public Page<ProductOrder> getAllActiveOrderPagination(Integer pageNo, Integer PageSize);
}
