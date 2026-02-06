package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // @Query("SELECT o FROM Order o WHERE o.userid.userId = :userId")
    // List<Order> findOrdersByUserId(@Param("userId") Long userId);

    List<Order> findByUser_UserId(Long userId);

    List<Order> findByArtisanOrders_Id(Long artisanId);

}
