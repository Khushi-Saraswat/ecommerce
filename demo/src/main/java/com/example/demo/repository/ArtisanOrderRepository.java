package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.ArtisanOrder;

public interface ArtisanOrderRepository extends JpaRepository<ArtisanOrder, Long> {

    // @Query("SELECT ao FROM ArtisanOrder where ao.order.id = :orderId")
    List<ArtisanOrder> findArtisanOrdersByOrder_Id(@Param("orderId") Long orderId);

}
