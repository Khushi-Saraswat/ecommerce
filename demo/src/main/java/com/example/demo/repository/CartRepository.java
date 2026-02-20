package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    public Cart findByProductId(Integer productId);

    // public Integer countByUserId(Long userId);

    @Query("Select c from Cart c  where c.user.userId = :userId")
    public List<Cart> findCartsByUserId(@Param("userId") Long userId);

    Optional<Cart> findByUser_UserIdAndProduct_Id(Long userId, Integer productId);

    void deleteByUser_UserId(Long userId);

    // public void deleteByProductId(Integer productId);

    /// public void deleteByUserId(Long userId);

}
