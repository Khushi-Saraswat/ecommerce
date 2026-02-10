package com.example.demo.response.Artisan;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ArtisanSaveResponse {

    private String message;
    private LocalDateTime createdAt;
}
