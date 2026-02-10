package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.Role;
import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.constants.errorTypes.CategoryError;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.Auth.AuthException;
import com.example.demo.exception.Category.CategoryException;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.request.category.CategoryRequestDTO;
import com.example.demo.response.category.CategoryResponseDTO;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.CategoryService;

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CategoryResponseDTO addCategory(CategoryRequestDTO dto, String jwt) {
        // 1) Auth
        User user = authService.getCurrentUser();
        if (user == null) {
            throw new AuthException("jwt token is missing", AuthErrorType.TOKEN_MISSING);
        }

        // 2) Role check
        if (user.getRole() == null || !user.getRole().equals(Role.ARTISAN)) {
            throw new UserException("Only artisan can add category", UserErrorType.UNAUTHORIZED);
        }

        // 3) Validate name
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new CategoryException("Category name is required", CategoryError.INVALID_CATEGORY_NAME);
        }

        String name = dto.getName().trim();

        // 4) Create slug
        String slug = name.toLowerCase().replaceAll("[^a-z0-9]+", "-");

        // 5) Duplicate check (by slug)
        Category existing = categoryRepository.findBySlug(slug).orElse(null);
        if (existing != null) {
            CategoryResponseDTO response = new CategoryResponseDTO();
            response.setId(existing.getId());
            response.setName(existing.getName());
            response.setSlug(existing.getSlug());
            response.setMessage("Category already exists (returned existing)");
            return response;
        }

        // 6) Save new
        Category category = abstractMapperService.toEntity(dto, Category.class);
        category.setSlug(slug);

        Category saved = categoryRepository.save(category);

        // 7) Response
        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setSlug(saved.getSlug());
        response.setMessage("Category created successfully");

        return response;
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findByActiveTrue().stream()
                .map(
                        cat -> {
                            return abstractMapperService.toDto(cat, CategoryResponseDTO.class);
                        }

                ).collect(Collectors.toList());

    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        String slug = generateSlug(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new CategoryException("Category with this name already exists",
                    CategoryError.CATEGORY_ALREADY_EXISTS);
        }

        Category category = abstractMapperService.toEntity(request, Category.class);

        if (category != null) {
            category = categoryRepository.save(category);
            return abstractMapperService.toDto(request, CategoryResponseDTO.class);
        }

        throw new CategoryException("category is not created", CategoryError.CATEGORY_IS_NOT_SAVED);

    }

    @Override
    public void deleteCategory(String categoryId) throws BadRequestException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("category is not found", CategoryError.CATEGORY_NOT_FOUND));

        // Check if category has products
        if (!productRepository.findProductsByCategory_Id(Long.valueOf(categoryId)).isEmpty()) {
            throw new BadRequestException("Cannot delete category with associated products");
        }

        // Check if category has sub-categories
        if (!categoryRepository.findByParentIdAndActiveTrue(categoryId).isEmpty()) {
            throw new BadRequestException("Cannot delete category with sub-categories");
        }

        category.setActive(false);
        categoryRepository.save(category);

    }

    @Override
    public String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }

    @Override
    public CategoryResponseDTO getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("category is not found", CategoryError.CATEGORY_NOT_FOUND));

        return abstractMapperService.toDto(category, CategoryResponseDTO.class);
    }

    @Override
    public CategoryResponseDTO getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new CategoryException("category is not found", CategoryError.CATEGORY_NOT_FOUND));

        return abstractMapperService.toDto(category, CategoryResponseDTO.class);
    }

    @Override
    public List<CategoryResponseDTO> getRootCategories() {

        return categoryRepository.findByParentIdIsNullAndActiveTrue().stream()
                .map(
                        cat -> {
                            return abstractMapperService.toDto(cat, CategoryResponseDTO.class);
                        }

                ).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponseDTO> getSubCategories(String parentId) {

        return categoryRepository.findByParentIdIsNullAndActiveTrue().stream()
                .map(
                        cat -> {
                            return abstractMapperService.toDto(cat, CategoryResponseDTO.class);
                        }

                ).collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO updateCategory(String categoryId, CategoryRequestDTO request) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("category is not found", CategoryError.CATEGORY_NOT_FOUND));

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            String newSlug = generateSlug(request.getName());

            if (categoryRepository.existsBySlug(newSlug) && !newSlug.equals(category.getSlug())) {

                throw new CategoryException("Category with this name already exists",
                        CategoryError.CATEGORY_ALREADY_EXISTS);

            }
            category.setName(request.getName());
            category.setSlug(newSlug);
        }

        if (request.getDescription() != null)
            category.setDescription(request.getDescription());

        if (request.getImage() != null)
            category.setImage(request.getImage());

        if (request.getParentId() != null)
            category.setParentId(request.getParentId());

        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(request.isActive());

        category = categoryRepository.save(category);
        return abstractMapperService.toDto(category, CategoryResponseDTO.class);

    }

}
