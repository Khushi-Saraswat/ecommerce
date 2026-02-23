package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ArtisanOrder;

public interface ArtisanOrderRepository extends JpaRepository<ArtisanOrder, Long> {

    // @Query("SELECT ao FROM ArtisanOrder where ao.order.id = :orderId")
    Page<ArtisanOrder> findArtisanOrdersByOrder_Id(Long orderId, Pageable pageable);

    Page<ArtisanOrder> findByArtisan_Id(Long userId, Pageable pageable);

    List<ArtisanOrder> findArtisanOrdersByOrder_Id(Long orderId);
}
