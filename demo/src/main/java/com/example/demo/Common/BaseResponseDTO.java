package com.example.demo.Common;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BaseResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
}
