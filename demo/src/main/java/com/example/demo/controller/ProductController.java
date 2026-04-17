package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.service.methods.ProductService;



@RestController
@RequestMapping("/api/products")
public class ProductController {


    @Autowired
    private ProductService productService;

    

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                productService.getActiveProductById(productId));
    }

    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
            @RequestParam String ProductName,
            @RequestParam Double price,
            @RequestParam Double mrp,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                productService.searchProducts(ProductName, pageable, price, mrp));
    }

    @GetMapping("/products/category/{categoryName}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                productService.getProductsByCategory(categoryName, pageable));
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category) {

        return ResponseEntity.ok(
                productService.getAllActiveProductPagination(page, size, category));
        }

    @GetMapping("/product")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
            ) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

}
