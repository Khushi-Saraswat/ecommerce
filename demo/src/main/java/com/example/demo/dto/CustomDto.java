package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomDto {
    Long totalElements;

    int totalPages;

    int number;

    int size;

}
