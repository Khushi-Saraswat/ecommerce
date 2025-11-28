package com.example.demo.service.methods;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.dto.CategoryDto;
import com.example.demo.model.Category;

public interface CategoryService {

      public Category saveCategory(CategoryDto categoryDto);

      public Boolean existCategory(CategoryDto categoryDto);

      public List<Category> getAllCategory();

      public Boolean deleteCategory(int id);

      public Category getCategoryById(int id);

      public List<Category> getAllActiveCategory();

      public Page<Category> getAllActiveCategoryPagination(Integer pageNo, Integer PageSize);

      public List<CategoryDto> searchCategory(String ch);
}
