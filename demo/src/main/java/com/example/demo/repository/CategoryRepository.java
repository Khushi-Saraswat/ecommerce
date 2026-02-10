package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

    

    Optional<Category> findByNameIgnoreCase(String name);
    List<Category> findByActiveTrue();
    List<Category> findByParentIdIsNullAndActiveTrue();
    List<Category> findByParentIdAndActiveTrue(String parentId);
    Optional<Category> findBySlug(String slug);
    boolean existsBySlug(String slug);

}
