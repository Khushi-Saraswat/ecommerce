package com.example.demo.response.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRequestResponseDTO {
    private Long id;
    private String requestedCategoryName;
    private String note;
    private String status;

    private Long userId;
    private String userName;

    private String createdAt;
}
