package com.example.demo.service.methods;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.model.Category;

public interface CategoryService {

      public Category saveCategory(Category category);

      public Boolean existCategory(Category categoryName);

      public List<Category> getAllCategory();

      public Boolean deleteCategory(int id);

      public Category getCategoryById(int id);

      public List<Category> getAllActiveCategory();

      public Page<Category> getAllActiveCategoryPagination(Integer pageNo, Integer PageSize);
}
