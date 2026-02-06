
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // find all order items that belong to an order (through ArtisanOrder -> Order)
    List<OrderItem> findByArtisanOrder_Order_Id(Long orderId);

}