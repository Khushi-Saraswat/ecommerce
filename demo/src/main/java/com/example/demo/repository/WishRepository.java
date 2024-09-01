package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Product;
import com.example.demo.model.Wish;

public interface WishRepository extends JpaRepository<Wish, Integer> {

      public Wish findByProductIdAndUserId(Integer productId, Integer userId);

      public List<Wish> findByUserId(Integer userId);

      public Wish findByProductId(Integer productId);

      public List<Product> findProductByUserId(Integer id);
}
