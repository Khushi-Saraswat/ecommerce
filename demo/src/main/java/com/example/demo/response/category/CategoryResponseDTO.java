package com.example.demo.response.category;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoryResponseDTO {

    private String id;
    private String name;
    private String description;
    private String image;
    private String slug;
    private boolean isActive;
    private LocalDateTime createdAt;
    private String message;

}
