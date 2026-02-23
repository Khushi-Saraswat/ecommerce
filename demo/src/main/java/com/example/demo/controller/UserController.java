package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FeedbackDto;
import com.example.demo.request.User.UserRequestDTO;
import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.response.User.UserResponseDTO;
import com.example.demo.response.category.CategoryResponseDTO;
import com.example.demo.service.methods.CategoryService;
import com.example.demo.service.methods.FeedbackService;
import com.example.demo.service.methods.ProductService;
import com.example.demo.service.methods.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    // ===============================
    // 🔹 USER PROFILE
    // ===============================

    // Get logged-in user profile
    @PreAuthorize("hasAnyRole('USER','ARTISAN')")
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    // Update profile (logged-in user)
    @PreAuthorize("hasAnyRole('USER','ARTISAN')")
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @RequestBody UserRequestDTO userRequestDTO) {

        return ResponseEntity.ok(userService.updateUserProfile(userRequestDTO));
    }

    // ===============================
    // 🔹 ORDER MANAGEMENT (USER)
    // ===============================

    // Get all orders of logged-in user
    /*
     * @PreAuthorize("hasRole('USER')")
     * 
     * @GetMapping("/orders")
     * public ResponseEntity<?> getMyOrders() {
     * return ResponseEntity.ok(userService.);
     * }
     */

    // Track specific order
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> trackOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(userService.trackOrder(orderId));
    }

    // Cancel order
    /*
     * @PreAuthorize("hasRole('USER')")
     * 
     * @PutMapping("/orders/{orderId}/cancel")
     * public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
     * return ResponseEntity.ok(userService.cancelOrder(orderId));
     * }
     */

    // ===============================
    // 🔹 FEEDBACK
    // ===============================

    // Add feedback
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/feedback")
    public ResponseEntity<FeedbackDto> addFeedback(
            @RequestBody FeedbackDto feedbackDto) {

        return ResponseEntity.ok(feedbackService.saveFeedBack(feedbackDto));
    }

    // Get feedback for product
    @GetMapping("/feedback/product/{productId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbackForProduct(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                feedbackService.getFeedbackByProductId(productId));
    }

    // ===============================
    // 🔹 CATEGORY APIs
    // ===============================

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(categoryId));
    }

    @GetMapping("/categories/slug/{slug}")
    public ResponseEntity<CategoryResponseDTO> getCategoryBySlug(
            @PathVariable String slug) {

        return ResponseEntity.ok(
                categoryService.getCategoryBySlug(slug));
    }

    @GetMapping("/categories/active")
    public ResponseEntity<List<CategoryResponseDTO>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/categories/search")
    public ResponseEntity<List<CategoryResponseDTO>> searchCategories(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                categoryService.searchCategories(keyword, pageable));
    }

    // ===============================
    // 🔹 PRODUCT APIs
    // ===============================

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                productService.getActiveProductById(productId));
    }

    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                productService.searchProducts(q, pageable));
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                productService.getProductsByCategory(categoryId, pageable));
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category) {

        return ResponseEntity.ok(
                productService.getAllActiveProductPagination(page, size, category));
    }
}