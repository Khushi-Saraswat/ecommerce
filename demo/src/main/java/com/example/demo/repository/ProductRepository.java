package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Artisan;
import com.example.demo.model.Product;
import com.example.demo.model.ProductImage;

public interface ProductRepository extends JpaRepository<Product, Integer> {

     // returns list of active product ..
     List<Product> findByIsActiveTrue();

     // return product for pagination if they are set to true..
     Page<Product> findByIsActiveTrue(Pageable pageable);

     // Search products by title or category name (case-insensitive)
     // @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%',
     // :searchTerm, '%')) OR LOWER(p.category.name) LIKE LOWER(CONCAT('%',
     // :searchTerm, '%'))")
     // List<ProductDto>
     // findByTitleOrCategoryNameContainingIgnoreCase(@Param("searchTerm") String
     // searchTerm);

     // Search with pagination
     // @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%',
     // :searchTerm, '%')) OR LOWER(p.category.name) LIKE LOWER(CONCAT('%',
     // :searchTerm, '%'))")
     // Page<Product>
     // findByTitleOrCategoryNameContainingIgnoreCase(@Param("searchTerm") String
     // searchTerm,
     // Pageable pageable);

     // return Pageable product when pagination is given along with category
     Page<Product> findByCategory(Pageable pageable, String category);

     List<Product> findByartId(int id);

     @Query("Select p.artisan from Product p where p.id = :productId")
     Artisan findArtisanByProductId(@Param("productId") Integer productId);

     List<ProductImage> findProductImagesById(Integer productId);

     // Find products by artisan city (case-insensitive) and active flag
     List<Product> findByArtisanCityIgnoreCaseAndIsActiveTrue(String city);

     // Find products by artisan state (case-insensitive) and active flag
     List<Product> findByArtisanStateIgnoreCaseAndIsActiveTrue(String state);

     List<Product> findProductsByCategory_Id(Long id);
}
