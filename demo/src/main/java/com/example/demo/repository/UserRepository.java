package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Long> {

    // public Optional<UserDtls> findByusername(String email);

    public UserDtls findByusername(String email);

    public List<UserDtls> findByroles(String role);

    public UserDtls findByResetToken(String token);

}
