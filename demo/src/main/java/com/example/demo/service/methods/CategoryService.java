package com.example.demo.service.methods;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.request.category.CategoryRequestDTO;
import com.example.demo.response.category.CategoryResponseDTO;

public interface CategoryService {

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryBySlug(String slug);

    String createCategory(CategoryRequestDTO request, MultipartFile file);

    String updateCategory(Long categoryId, CategoryRequestDTO request, MultipartFile file) throws Exception;

    String deleteCategory(Long categoryId) throws BadRequestException, Exception;

    String generateSlug(String name);

    CategoryResponseDTO getCategoryById(Long categoryId);

    List<CategoryResponseDTO> getActiveCategories();

    List<CategoryResponseDTO> searchCategories(String keyword);

}
