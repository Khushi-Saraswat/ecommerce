package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // @Query("SELECT o FROM Order o WHERE o.userid.userId = :userId")
    // List<Order> findOrdersByUserId(@Param("userId") Long userId);

    Page<Order> findByUser_UserId(Long userId, Pageable pageable);

    List<Order> findByArtisanOrders_Id(Long artisanId);

}
