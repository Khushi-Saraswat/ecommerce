package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
import com.example.demo.dto.ProductDto;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.methods.CategoryService;
import com.example.demo.service.methods.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // this method is used to delete a particular product by it's id
    @GetMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        System.out.println(id + "in deleting product from admin side");
        Boolean deleteProduct = productService.deleteProduct(id);
        // code 200 -successful deletion
        if (deleteProduct) {
            return new ResponseEntity<>("Product is deleted successfully !!!", HttpStatusCode.valueOf(200));
        }
        // code 409 Conflict: If the resource cannot be deleted due to a conflict
        else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        }

    }

    // this method is responsible for saving the product of a particular category
    @PostMapping("/saveProduct")
    public ResponseEntity<String> saveProduct(Product product, @RequestParam("file") MultipartFile image)
            throws IOException {
        Category category = product.getCategory();
        // first of all check whether category exist or not
        Boolean isExist = categoryService.existCategory(modelMapper.map(category, CategoryDto.class));

        // if category exist we will proceed and saved the product and save the image in
        // local directory
        if (isExist) {

            String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

            product.setImage(imageName);
            product.setDiscount(0);
            product.setDiscountPrice(product.getPrice());
            Product saveProduct = productService.saveProduct(product);

            try (InputStream inputStream = image.getInputStream()) {
                Path path = Paths.get("C:\\Users\\DELL\\Desktop\\ecommerce\\demo\\src\\main\\resources\\static\\img"
                        + File.separator + "product_img" + File.separator
                        + image.getOriginalFilename());

                // System.out.println(path);
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            // if product saved
            if (saveProduct != null)
                return new ResponseEntity<>("Product saved successfully !!", HttpStatus.valueOf(200));

            // if product is not saved
            else
                return new ResponseEntity<>("Product is not saved", HttpStatusCode.valueOf(500));

        }
        // category does not exist
        else {
            return ResponseEntity.ok("Category does not exist");
        }

    }

    @GetMapping("/product")
    public String ViewProduct(Model e, @RequestParam(defaultValue = "") String ch,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {
        /*
         * List<Product> products = null;
         * if (ch != null && ch.length() > 0) {
         * products = productService.searchProduct(ch);
         * } else {
         * products = productService.getAllProducts();
         * }
         */

        Page<Product> page = null;
        if (ch != null && ch.length() > 0) {
            page = productService.searchProductPagination(pageNo, pageSize, ch);
        } else {
            page = productService.getAllProducts(pageNo, pageSize);
        }
        e.addAttribute("products", page.getContent());
        e.addAttribute("pageNo", page.getNumber());
        System.out.println(page.getNumber() + "pagenumber");
        e.addAttribute("pageSize", pageSize);
        System.out.println(pageSize + "pageSize");
        e.addAttribute("totalElements", page.getTotalElements());
        System.out.println("totalElements" + page.getTotalElements());
        e.addAttribute("totalPages", page.getTotalPages());
        System.out.println("totalPages" + page.getTotalElements());
        e.addAttribute("isFirst", page.isFirst());
        System.out.println("isFirst" + page.isFirst());
        e.addAttribute("isLast", page.isLast());
        System.out.println("isLast" + page.isLast());
        return "admin/product";
    }

    // this method is responsible for updating a product.
    @PostMapping("/updateProduct")
    public ResponseEntity<String> editProduct(Product product, @RequestParam("file") MultipartFile image) {

        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            ResponseEntity.ok("invalid Discount");
        } else {
            Product updateProduct = productService.updateProduct(product, image);
            if (!ObjectUtils.isEmpty(updateProduct)) {
                return new ResponseEntity<>("Product is updated sucessfully !!!", HttpStatusCode.valueOf(200));
            }

        }

        return new ResponseEntity<>("Product  is not updated successfully", HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/product/{id}/activate")
    public ResponseEntity<String> activeCategory(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (!ObjectUtils.isEmpty(product)) {
            product.setIsActive(true);
            productService.saveProduct(modelMapper.map(product, ProductDto.class));
            return new ResponseEntity<>("Product activated successfully", HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>("Product is not activated successfully", HttpStatus.valueOf(404));

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

}
