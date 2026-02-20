package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ArtisanOrder;

public interface ArtisanOrderRepository extends JpaRepository<ArtisanOrder, Long> {

    // @Query("SELECT ao FROM ArtisanOrder where ao.order.id = :orderId")
    List<ArtisanOrder> findArtisanOrdersByOrder_Id(Long orderId);

    List<ArtisanOrder> findByArtisan_Id(Long orderId);
}
