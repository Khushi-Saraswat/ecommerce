package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.dto.Products;
import com.example.demo.request.User.UserRequestDTO;
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

    // access products

    // GET /api/products/all
    @GetMapping("/all")
    public ResponseEntity<List<Products>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // GET /api/products/{productId}
    @GetMapping("/{productId}")
    public ResponseEntity<Products> getProductById(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    // GET /api/products/category/{categoryId}
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Products>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(userService.getProductsByCategory(categoryId));
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