package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.errorTypes.CategoryError;
import com.example.demo.exception.Category.CategoryException;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.request.category.CategoryRequestDTO;
import com.example.demo.response.category.CategoryResponseDTO;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // ✅ Get All Active Categories
    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(cat -> abstractMapperService.toDto(cat, CategoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    // ✅ Create Category (with Cloudinary Image)
    @Override
    public String createCategory(CategoryRequestDTO request, MultipartFile file) {

        if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
            throw new CategoryException("Category name is required", CategoryError.CATEGORY_NOT_FOUND);
        }

        String slug = generateSlug(request.getName().trim());

        if (categoryRepository.existsBySlug(slug)) {
            throw new CategoryException("Category with this name already exists",
                    CategoryError.CATEGORY_ALREADY_EXISTS);
        }

        Category category = abstractMapperService.toEntity(request, Category.class);

        category.setName(request.getName().trim());
        category.setSlug(slug);

        // ✅ Upload Image to Cloudinary
        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(file);
            category.setImageUrl(imageUrl);
        }

        Category saved = categoryRepository.save(category);

        if (saved == null) {
            throw new CategoryException("category is not created", CategoryError.CATEGORY_IS_NOT_SAVED);
        }

        return "create category successfully !!";
    }

    // ✅ Update Category (with Cloudinary Image Replace)
    @Override
    public String updateCategory(Long categoryId, CategoryRequestDTO request, MultipartFile file)
            throws NumberFormatException, Exception {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("category is not found", CategoryError.CATEGORY_NOT_FOUND));

        // ✅ Update Name + Slug
        if (request.getName() != null && !request.getName().trim().isEmpty()
                && !request.getName().equals(category.getName())) {

            String newSlug = generateSlug(request.getName().trim());

            if (categoryRepository.existsBySlug(newSlug) && !newSlug.equals(category.getSlug())) {
                throw new CategoryException("Category with this name already exists",
                        CategoryError.CATEGORY_ALREADY_EXISTS);
            }

            category.setName(request.getName().trim());
            category.setSlug(newSlug);
        }

        // ✅ Update Description
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        // ✅ Update Active
        category.setIsActive(request.isActive());

        // ✅ IMAGE UPDATE
        if (file != null && !file.isEmpty()) {

            // delete old image
            if (category.getImage() != null && !category.getImage().isEmpty()) {
                cloudinaryService.deleteImage(category.getImageUrl());
            }

            // upload new image
            String newImageUrl = cloudinaryService.uploadImage(file);
            category.setImage(newImageUrl);
        }

        categoryRepository.save(category);

        return "category is updated successfully";
    }

    // ✅ Soft Delete Category + Cloudinary Delete
    @Override
    public String deleteCategory(Long categoryId) throws BadRequestException, Exception {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("category is not found", CategoryError.CATEGORY_NOT_FOUND));

        // Check if category has products
        if (!productRepository.findProductsByCategory_Id(categoryId).isEmpty()) {
            throw new BadRequestException("Cannot delete category with associated products");
        }

        // soft delete
        category.setIsActive(false);

        // delete cloudinary image
        if (category.getImage() != null && !category.getImage().isEmpty()) {
            cloudinaryService.deleteImage(category.getImageUrl());
            category.setImage(null);
        }

        categoryRepository.save(category);

        return "category is successfully deleted";
    }

    // ✅ Generate Slug
    @Override
    public String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }

    // ✅ Get Category by Slug
    @Override
    public CategoryResponseDTO getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new CategoryException("category is not found", CategoryError.CATEGORY_NOT_FOUND));

        return abstractMapperService.toDto(category, CategoryResponseDTO.class);
    }

    // ✅ Get Active Categories
    @Override
    public List<CategoryResponseDTO> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(c -> abstractMapperService.toDto(c, CategoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    // ✅ Get Category by Id
    @Override
    public CategoryResponseDTO getCategoryById(Long categoryId) throws NumberFormatException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("category is not found", CategoryError.CATEGORY_NOT_FOUND));
        return abstractMapperService.toDto(category, CategoryResponseDTO.class);
    }

    // ✅ Search Categories
    @Override
    public List<CategoryResponseDTO> searchCategories(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        return categoryRepository.findByNameIgnoreCase(keyword.trim())
                .stream()
                .map(c -> abstractMapperService.toDto(c, CategoryResponseDTO.class))
                .toList();
    }

}
