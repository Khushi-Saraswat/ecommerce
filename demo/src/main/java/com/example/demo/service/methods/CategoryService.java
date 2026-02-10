package com.example.demo.service.methods;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.example.demo.request.category.CategoryRequestDTO;
import com.example.demo.response.category.CategoryResponseDTO;

public interface CategoryService {

    CategoryResponseDTO addCategory(CategoryRequestDTO dto, String jwt);

    List<CategoryResponseDTO> getAllCategories();

    List<CategoryResponseDTO> getRootCategories();

    List<CategoryResponseDTO> getSubCategories(String parentId);

    CategoryResponseDTO getCategoryById(String categoryId);

    CategoryResponseDTO getCategoryBySlug(String slug);

    CategoryResponseDTO createCategory(CategoryRequestDTO request);

    CategoryResponseDTO updateCategory(String categoryId, CategoryRequestDTO request);

    void deleteCategory(String categoryId) throws BadRequestException;

    String generateSlug(String name);

}
