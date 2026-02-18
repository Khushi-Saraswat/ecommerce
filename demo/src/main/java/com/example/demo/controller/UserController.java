package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import com.example.demo.response.category.CategoryResponseDTO;
import com.example.demo.service.methods.CategoryService;
import com.example.demo.service.methods.FeedbackService;
import com.example.demo.service.methods.ProductService;
import com.example.demo.service.methods.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // endpoint to get user profile details

    @PreAuthorize("hasAnyRole('USER','ARTISAN')")
    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        // UserResponseDTO>

        return ResponseEntity.ok("hi");
        // userService.UserByToken());

    }

    // endpoint to update user profile details

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestParam Long userId,

            @RequestBody UserRequestDTO userRequestDTO) {

        return ResponseEntity.ok(userService.updateUserProfile(userId, userRequestDTO));
    }

    // endpoint for order tracking
    @GetMapping("/order/tracking/{orderId}")
    public ResponseEntity<?> trackOrder(@PathVariable String orderId) {

        return ResponseEntity.ok(userService.trackOrder(orderId));

    }

    @PostMapping("feedback/add")
    public ResponseEntity<String> saveFeed(@RequestBody FeedbackDto feedbackDto) {
        FeedbackDto feedback = feedbackService.saveFeedBack(feedbackDto);

        if (feedback != null) {
            return ResponseEntity.ok("Feedback is successfully saved");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Feedback could not be saved");
        }

    }

    @GetMapping("feedback/product/{productId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbackForProduct(@PathVariable Integer ProductId) {

        List<FeedbackDto> feedback = feedbackService.getFeedbackByProductId(ProductId);

        return ResponseEntity.ok(feedback);
    }

    // <-------- category mgmt------------>

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {

        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // GET /api/categories/{categoryId}

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    // GET /api/categories/slug/{slug}

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponseDTO> getCategoryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getCategoryBySlug(slug));
    }

    // GET /api/categories/active

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponseDTO>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    // GET /api/categories/search?keyword=...
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponseDTO>> searchCategories(@RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(categoryService.searchCategories(keyword, pageable));
    }

    // product management .....

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer productId) {

        return ResponseEntity.ok(productService.getActiveProductById(productId));

    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(productService.searchProducts(q, pageable));

    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(@PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam String category

    ) {
        return ResponseEntity.ok(productService.getAllActiveProductPagination(page, size, category));
    }

}

// flow of spring security jwt
/*
 * Here’s the **most short + clear** version:
 ** 
 * Login phase:**
 * 
 * User sends username/password.
 * `AuthenticationManager` + `UserDetailsService` fetch user and verify password
 * using `PasswordEncoder`.
 * If correct → `JwtService` creates JWT and returns it.
 ** 
 * Secured request phase:**
 * 
 * User sends JWT in `Authorization` header.
 * `JwtAuthFilter` intercepts, extracts token, gets username from token,
 * validates token.
 * Loads user again using `UserDetailsService`.
 * Sets authenticated user in `SecurityContext`.
 ** 
 * Roles summary:**
 * 
 * **SecurityFilter / JwtAuthFilter:** Catches every request, reads/validates
 * JWT, sets auth context.
 * **JwtService:** Creates/validates/parses tokens.
 * **UserDetailsService:** Fetches user from DB.
 * **PasswordEncoder:** Checks raw vs hashed password.
 * **SecurityContext:** Stores who is logged in for that request.
 */