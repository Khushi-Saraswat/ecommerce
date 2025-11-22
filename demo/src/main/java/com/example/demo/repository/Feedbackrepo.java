package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.model.Feedback;

public interface Feedbackrepo extends JpaRepository<Feedback,Integer>{
  long countByCategory(String Category);

   List<Feedback> findByProductId(Integer ProductId);
}
