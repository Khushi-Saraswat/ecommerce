package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.methods.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Public get categories (dropdown)
    /*
     * *@GetMapping("/categories")
     * public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>>
     * getAllCategories() {
     * List<CategoryResponseDTO> categories = categoryService.getAllCategories();
     * return ResponseEntity.ok(ApiResponse.success(categories));
     * }
     * 
     * @GetMapping
     * public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>>
     * getRootCategories() {
     * List<CategoryResponseDTO> category = categoryService.getRootCategories();
     * return ResponseEntity.ok(ApiResponse.success(category));
     * }
     * 
     * public ResponseEntity<ApiResponse<CategoryResponseDTO>>
     * getCategoryBySlug(@PathVariable String slug) {
     * CategoryResponseDTO category = categoryService.getCategoryBySlug(slug);
     * return ResponseEntity.ok(ApiResponse.success(category));
     * }
     * 
     * @GetMapping("/{id}/subcategories")
     * public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>>
     * getSubCategories(@PathVariable String id) {
     * List<CategoryResponseDTO> categories = categoryService.getSubCategories(id);
     * return ResponseEntity.ok(ApiResponse.success(categories));
     * }
     */

}
