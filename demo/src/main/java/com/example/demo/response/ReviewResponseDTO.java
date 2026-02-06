package com.example.demo.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long id;
    private Integer productId;
    private Long userId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

}
