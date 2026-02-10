package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.constants.RequestStatus;
import com.example.demo.model.CategoryRequest;

public interface CategoryRequestRepository extends JpaRepository<CategoryRequest, Long> {

    List<CategoryRequest> findByStatus(RequestStatus status);

    boolean existsByRequestedCategoryNameIgnoreCaseAndStatus(String name, RequestStatus status);

    boolean existsByNameIgnoreCase(String categoryName);
}
