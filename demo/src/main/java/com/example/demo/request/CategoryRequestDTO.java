package com.example.demo.request;

import lombok.Data;

@Data
public class CategoryRequestDTO {
    private String name;

    private String description;
    private String image;
    private String parentId;
    private int displayOrder;
    private boolean active;
}
