package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.request.category.CategoryNameRequest;
import com.example.demo.response.category.CategoryRequestResponseDTO;

public interface CategoryRequestService {
    // USER/ARTISAN
    CategoryRequestResponseDTO createRequest(CategoryNameRequest dto);

    List<CategoryRequestResponseDTO> myRequests();

    // ADMIN
    List<CategoryRequestResponseDTO> getPendingRequests();

    String approveRequest(Long requestId);

    String rejectRequest(Long requestId, String reason);
}
