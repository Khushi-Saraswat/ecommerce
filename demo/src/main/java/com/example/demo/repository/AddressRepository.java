
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Address;
import com.example.demo.model.User;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("SELECT a FROM Address a WHERE a.user.userId = :userId")
    List<Address> findAddressByUserId(@Param("userId") Long userId);

    Address findByUser(User user);

    // Find default address by user ID
    @Query("SELECT a FROM Address a WHERE a.user.userId = :userId AND a.defaultAddress = true")
    Address findDefaultAddressByUserId(@Param("userId") Long userId);

}
