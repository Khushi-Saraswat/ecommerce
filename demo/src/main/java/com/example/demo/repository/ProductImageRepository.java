package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

}
