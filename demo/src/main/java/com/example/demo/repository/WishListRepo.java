package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Wishlist;

public interface WishListRepo extends JpaRepository<Wishlist, Long> {

    Wishlist findByUser_UserId(Long userId);

}
