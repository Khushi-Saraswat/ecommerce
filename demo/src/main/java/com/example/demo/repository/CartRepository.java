package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findByProductIdAndUserId(Integer productId, Long userId);

    public Integer countByUserId(Long userId);

    public List<Cart> findByUserId(Long userId);

    public void deleteByProductId(Integer productId);

}
