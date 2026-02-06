package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.PriceHistory;

public interface PriceHistoryRepo extends JpaRepository<PriceHistory, Integer> {

}
