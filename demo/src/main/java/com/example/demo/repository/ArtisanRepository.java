package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Artisan;

public interface ArtisanRepository extends JpaRepository<Artisan, Long> {

    Optional<Artisan> findByUserUserId(Long userId);

    Artisan findArtisanById(Long id);

}
