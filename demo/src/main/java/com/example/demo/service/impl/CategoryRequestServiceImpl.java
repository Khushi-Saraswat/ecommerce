package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.RequestStatus;
import com.example.demo.constants.errorTypes.CategoryError;
import com.example.demo.exception.Category.CategoryException;
import com.example.demo.model.CategoryRequest;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRequestRepository;
import com.example.demo.request.category.CategoryNameRequest;
import com.example.demo.response.category.CategoryRequestResponseDTO;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.CategoryRequestService;

@Service
public class CategoryRequestServiceImpl implements CategoryRequestService {

    @Autowired
    private AuthService authservice;

    @Autowired
    private CategoryRequestRepository categoryRequestRepository;

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Override
    public String approveRequest(Long requestId) {

        CategoryRequest categoryRequest = categoryRequestRepository.findById(requestId)
                .orElseThrow(
                        () -> new CategoryException("Request not found", CategoryError.CATEGORY_REQUEST_NOT_FOUND));

        if (categoryRequest.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is already processed");
        }

        boolean categoryExists = categoryRequestRepository
                .existsByNameIgnoreCase(categoryRequest.getRequestedCategoryName());
        if (categoryExists) {
            categoryRequest.setStatus(RequestStatus.REJECTED);
            categoryRequest.setUpdatedAt(LocalDateTime.now());
            categoryRequestRepository.save(categoryRequest);
            return "Category already exists, request auto rejected";
        }

        CategoryRequest category = new CategoryRequest();
        category.setRequestedCategoryName(categoryRequest.getRequestedCategoryName());
        category.setStatus(RequestStatus.APPROVED);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRequestRepository.save(category);

        return "Request approved and category created successfully";

    }

    @Override
    public CategoryRequestResponseDTO createRequest(CategoryNameRequest dto) {
        User user = authservice.getCurrentUser();

        // prevent duplicates (pending)
        boolean alreadyPending = categoryRequestRepository
                .existsByRequestedCategoryNameIgnoreCaseAndStatus(
                        dto.getRequestedCategoryName(),
                        RequestStatus.PENDING);

        if (alreadyPending) {
            throw new RuntimeException("This category request is already pending");
        }

        CategoryRequest categoryRequest = abstractMapperService.toEntity(dto, CategoryRequest.class);

        CategoryRequest category = categoryRequestRepository.save(categoryRequest);

        return abstractMapperService.toDto(category, CategoryRequestResponseDTO.class);

    }

    @Override
    public List<CategoryRequestResponseDTO> getPendingRequests() {

        return categoryRequestRepository.findByStatus(RequestStatus.PENDING)
                .stream().map(
                        cat -> {
                            return abstractMapperService.toDto(cat, CategoryRequestResponseDTO.class);
                        })
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryRequestResponseDTO> myRequests() {

        User user = authservice.getCurrentUser();

        categoryRequestRepository.findAll().stream()
                .map(
                        cat -> {
                            return abstractMapperService.toDto(cat, CategoryRequestResponseDTO.class);
                        })
                .collect(Collectors.toList());

    }

    @Override
    public String rejectRequest(Long requestId, String reason) {
        CategoryRequest categoryRequest = categoryRequestRepository.findById(requestId)
                .orElseThrow(
                        () -> new CategoryException("Request not found", CategoryError.CATEGORY_REQUEST_NOT_FOUND));

        if (categoryRequest.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is already processed");
        }

        categoryRequest.setStatus(RequestStatus.REJECTED);

        // optional: store reason in note
        if (reason != null && !reason.isBlank()) {
            categoryRequest.setNote("Rejected Reason: " + reason);
        }

        categoryRequest.setUpdatedAt(LocalDateTime.now());
        categoryRequestRepository.save(categoryRequest);

        return "Request rejected successfully";
    }

}
