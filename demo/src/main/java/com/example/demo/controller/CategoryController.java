package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.response.category.CategoryResponseDTO;
import com.example.demo.service.methods.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    // ===============================
    // 🔹 CATEGORY APIs
    // ===============================\

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        System.out.println(categoryService.getAllCategories() + "all categories");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(categoryId));
    }

    @GetMapping("/categories/slug/{slug}")
    public ResponseEntity<CategoryResponseDTO> getCategoryBySlug(
            @PathVariable String slug) {

        return ResponseEntity.ok(
                categoryService.getCategoryBySlug(slug));
    }

    @GetMapping("/categories/active")
    public ResponseEntity<List<CategoryResponseDTO>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/categories/search")
    public ResponseEntity<List<CategoryResponseDTO>> searchCategories(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                categoryService.searchCategories(keyword, pageable));
    }

}
