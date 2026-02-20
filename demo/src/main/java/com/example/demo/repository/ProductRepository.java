package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Artisan;
import com.example.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

     // return product for pagination if they are set to true..
     Page<Product> findByIsActiveTrue(Pageable pageable);

     @Query("""
               SELECT p FROM Product p
               WHERE p.isActive = true AND
               (
                    LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                    OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               )
               """)
     Page<Product> findProductsBySearchText(@Param("searchTerm") String searchTerm,
               Pageable pageable);

     // return Pageable product when pagination is given along with category
     Page<Product> findByCategory(Pageable pageable, String category);

     Page<Product> findByartId(int id, Pageable pageable);

     @Query("Select p.artisan from Product p where p.id = :productId")
     Artisan findArtisanByProductId(@Param("productId") Integer productId);

     Optional<Product> findByIdAndIsActiveTrue(Integer id);

     Optional<Product> findBySlugAndIsActiveTrue(String slug);

     // Find products by artisan city (case-insensitive) and active flag
     List<Product> findByArtisanCityIgnoreCaseAndIsActiveTrue(String city);

     // Find products by artisan state (case-insensitive) and active flag
     List<Product> findByArtisanStateIgnoreCaseAndIsActiveTrue(String state);

     List<Product> findProductsByCategory_Id(Long id);

}
