package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Search with pagination
    @Query("SELECT c FROM Category c WHERE" +
            "LOWER(c.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR" +
            "LOWER(c.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))"

    )
    Page<Category> findCategoriesBySearchText(@Param("searchTerm") String searchTerm,
            Pageable pageable);

    // Optional<Category> findByNameIgnoreCase(String name);

    List<Category> findByIsActiveTrue();

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

}
