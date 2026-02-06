package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.constants.Role;
import com.example.demo.dto.OrderDto;
import com.example.demo.model.Address;
import com.example.demo.model.Cart;
import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // public Optional<UserDtls> findByusername(String email);

    public User findByusername(String email);

    // public List<User> findByroles(String role);

    public User findByResetToken(String token);

    public User findByUserId(long userId);

    public List<OrderDto> findOrdersByUserId(Long userId);

    public List<User> findByrole(Role role);

    public Address findAddressByUserId(long userId);

    public List<Cart> findCartsByUserId(long userId);

}
