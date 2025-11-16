package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

     // returns list of active product ..
     List<Product> findByIsActiveTrue();

     // return product for pagination if they are set to true..
     Page<Product> findByIsActiveTrue(Pageable pageable);

     // return list of product by category
     List<Product> findByCategory(String category);

     List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

     Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
               Pageable pageable);

     // return Pageable product when pagination is given along with category
     Page<Product> findByCategory(Pageable pageable, String category);
}
