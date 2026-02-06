package com.example.demo.request;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private Integer productId;
    private Long userId;
    private Integer rating;
    private String comment;
}
