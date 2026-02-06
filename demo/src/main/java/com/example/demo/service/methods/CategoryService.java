package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.request.CategoryRequestDTO;
import com.example.demo.response.CategoryResponseDTO;

public interface CategoryService {

    CategoryResponseDTO addCategory(CategoryRequestDTO dto, String jwt);

    List<CategoryResponseDTO> getAllCategories();

    List<CategoryResponseDTO> getRootCategories();

    List<CategoryResponseDTO> getSubCategories(String parentId);

    CategoryResponseDTO getCategoryById(String categoryId);

    CategoryResponseDTO getCategoryBySlug(String slug);

    CategoryResponseDTO createCategory(CategoryRequestDTO request);

    CategoryResponseDTO updateCategory(String categoryId, CategoryRequestDTO request);

    void deleteCategory(String categoryId);

    String generateSlug(String name);

}
