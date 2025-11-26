package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

     List<ProductOrder> findByUserId(Long userId);

     ProductOrder findByOrderId(String orderId);

}
