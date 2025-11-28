package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

      // checks whether the category exist by name or not..
      public Boolean existsByName(String name);

      // Find list of category if they are active by admin.
      public List<Category> findByIsActiveTrue();

      // Return Pagination of category when pageable for active category is true.
      Page<Category> findByIsActiveTrue(Pageable pageable);

      public List<Category> findByNameContainingIgnoreCase(String ch);

}
