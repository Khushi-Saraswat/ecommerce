package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Order;

public interface ProductOrderRepository extends JpaRepository<Order, Integer> {

     // List<ProductOrder> findByUserUserId(Long userId);

     // ProductOrder findByOrderId(Long orderId);

}
