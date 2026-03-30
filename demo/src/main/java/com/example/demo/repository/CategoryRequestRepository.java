package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.constants.RequestStatus;
import com.example.demo.model.CategoryRequest;

public interface CategoryRequestRepository extends JpaRepository<CategoryRequest, Long> {

    List<CategoryRequest> findByStatus(RequestStatus status);

    boolean existsByStatus(RequestStatus status);

    // boolean existsByNameIgnoreCaseAndStatus(String categoryName, RequestStatus
    // status);

    boolean existsByNameIgnoreCase(String categoryName);

    List<CategoryRequest> findByName(String categoryName);

    boolean existsByNameIgnoreCaseAndStatusAndArtisan_Id(
            String name,
            RequestStatus status,
            Long artisanId);

}
