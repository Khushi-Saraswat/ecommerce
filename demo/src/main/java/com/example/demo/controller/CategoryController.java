package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.CategoryExist;
import com.example.demo.model.Category;
import com.example.demo.service.methods.CategoryService;

//this is category controller that is used to manage all the category related api...
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    // this method is used to load all the existing category that is added by admin.
    @GetMapping("/Category")
    public ResponseEntity<List<Category>> loadCategory(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {

        // get page requested for particular category
        Page<Category> page = categoryService.getAllActiveCategoryPagination(pageNo, pageSize);
        // get content from page object
        List<Category> category = page.getContent();
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    // this method is used to save-category added by admin.
    @PostMapping("/saveCategory")
    public ResponseEntity<String> AddCategory(CategoryDto category,
            @RequestParam("img") MultipartFile file) throws IOException {

        // check if file is empty or not
        if (file == null) {
            System.out.println("file is empty");
        } else {
            System.out.println("file is not empty");
        }
        // assign the name of file in this ...
        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);
        // and if category already exist show error message
        Boolean existCategory = categoryService.existCategory(category);

        if (existCategory) {
            // session.setAttribute("errorMsg", "Category Name already exists");
            throw new CategoryExist("Category already exist");
            // throw error
        } else {
            // saves the category
            Category saveCategory = categoryService.saveCategory(category);

            // if the category is not saved show error message.
            if (ObjectUtils.isEmpty(saveCategory)) {
                return new ResponseEntity<>("Category is not saved", HttpStatusCode.valueOf(500));
            } else {

                // save the image in folder
                try (InputStream inputStream = file.getInputStream()) {
                    Path path = Paths.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
                            + File.separator + "category_img" + File.separator
                            + file.getOriginalFilename());
                    System.out.println(path);
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

            }
        }

        return new ResponseEntity<>("Category saved successfully !!", HttpStatus.valueOf(200));

    }

    // this method is used to delete the category by id.
    @GetMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {

        // get category by id.
        Boolean deleteCategory = categoryService.deleteCategory(id);
        if (deleteCategory) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        }

    }

    // this method is used to update category
    @PostMapping("/updateCategory")
    public ResponseEntity<String> UpdateCategory(CategoryDto categoryDto, @RequestParam("file") MultipartFile file) {
        // find old category by it's id.
        Category oldcategory = categoryService.getCategoryById(categoryDto.getId());

        // get imageName
        String imageName = file.isEmpty() ? oldcategory.getImageName() : file.getOriginalFilename();
        // if edit category object is not null

        if (!ObjectUtils.isEmpty(categoryDto)) {
            oldcategory.setName(modelMapper.map(categoryDto, Category.class).getName());
            oldcategory.setIsActive(modelMapper.map(categoryDto, Category.class).getIsActive());
            oldcategory.setImageName(imageName);
        }
        // save the category
        Category updateCategory = categoryService.saveCategory(modelMapper.map(oldcategory, CategoryDto.class));
        // if updatedcategory is not empty
        if (!ObjectUtils.isEmpty(updateCategory)) {
            if (!file.isEmpty()) {

                // convert file into inputstream
                try (InputStream inputStream = file.getInputStream()) {
                    Path path = Paths.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
                            + File.separator + "category_img" + File.separator
                            + file.getOriginalFilename());

                    System.out.println(path);
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

            }

            return new ResponseEntity<>("Category is updated sucessfully !!!", HttpStatusCode.valueOf(200));

        } else {

            return new ResponseEntity<>("Category is not updated sucessfully", HttpStatus.NOT_MODIFIED);
        }

    }

    // this method is used to activate category by id.
    @PutMapping("/category/{id}/activate")
    public ResponseEntity<String> activeCategory(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        if (!ObjectUtils.isEmpty(category)) {
            category.setIsActive(true);
            categoryService.saveCategory(modelMapper.map(category, CategoryDto.class));
            return new ResponseEntity<>("Category activated successfully", HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>("Category not found", HttpStatus.valueOf(404));

    }

    // this method is used to deactivate category by id.
    @PutMapping("/category/{id}/deactivate")
    public ResponseEntity<String> deactivateCategory(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        if (!ObjectUtils.isEmpty(category)) {
            category.setIsActive(false);
            categoryService.saveCategory(modelMapper.map(category, CategoryDto.class));
            return new ResponseEntity<>("Category deactivated successfully", HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>("Category not found", HttpStatus.valueOf(404));

    }

    // fetch category with products......

    // search category by name
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDto>> searchProduct(@RequestParam String ch) {
        // get list of search product ....
        List<CategoryDto> categoryDtos = categoryService.searchCategory(ch);
        return ResponseEntity.ok(categoryDtos);
    }

}
