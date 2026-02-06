package com.example.demo.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ArtisanSaveResponse {

    private String message;
    private LocalDateTime createdAt;
}
