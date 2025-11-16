package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // This method saves the category....
    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // This method returns all the category ..
    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    // This method checks that whether category exist by name or not
    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }

    // This method is responsible for deleting category by id.
    @Override
    public Boolean deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(category)) {
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    // This method is responsible for returning the category by id
    @Override
    public Category getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category;
    }

    // This method is responsible for returning all the active category
    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> category = categoryRepository.findByIsActiveTrue();
        return category;
    }

    // This method is responsible for returning all the active category with
    // pagination.
    @Override
    public Page<Category> getAllActiveCategoryPagination(Integer pageNo, Integer PageSize) {

        Pageable pageable = PageRequest.of(pageNo, PageSize);
        return categoryRepository.findAll(pageable);

    }
    /*
     * Core pagination in spring boot is primarily implemented using
     * the PagingAndSortingRepository interface
     * PagingAndSortingRepository: This interface, part of Spring Data JPA,
     * provides built-in methods like findAll(Pageable pageable) to handle
     * pagination requests automatically.
     * Pageable: This interface is an object that holds the necessary pagination
     * information,
     * such as the current page number, the size of the page, and sorting
     * preferences. It is typically passed as a parameter to the repository methods.
     * Page: When the repository method executes, it returns a Page object.
     * This object is a sublist of objects that also includes metadata like the
     * total number of pages, total elements, and whether the current page is the
     * first or last one.
     */

}
