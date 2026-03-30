package com.example.demo.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
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

import com.example.demo.request.FeedBack.FeedbackDto;
import com.example.demo.request.Order.OrderRequestDTO;
import com.example.demo.request.User.UserRequestDTO;
import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.response.User.UserResponseDTO;
import com.example.demo.service.methods.CategoryService;
import com.example.demo.service.methods.FeedbackService;
import com.example.demo.service.methods.OrderService;
import com.example.demo.service.methods.ProductService;
import com.example.demo.service.methods.UserService;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('USER')")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;
    // ===============================
    // 🔹 USER PROFILE
    // ===============================

    // Get logged-in user profile
    
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    // Update profile (logged-in user)
    
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
            @RequestBody FeedbackDto feedbackDto
        ) {

        return ResponseEntity.ok(feedbackService.saveFeedBack(feedbackDto));
    }

    @PreAuthorize("hasRole('USER')")
    // Get feedback for product
    @GetMapping("/feedback/product/{productId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbackForProduct(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                feedbackService.getFeedbackByProductId(productId));
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
            @RequestParam Double price,
            @RequestParam Double mrp,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                productService.searchProducts(q, pageable, price, mrp));
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

    // orders
      // place order
     @PostMapping("/")
     public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDTO orderRequestDto) {

          return ResponseEntity.ok(orderService.saveOrder(orderRequestDto));
     }

     // Customer Order History
     @GetMapping("/history")
     public ResponseEntity<?> getOrderHistory(
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size) {

          return ResponseEntity
                    .ok(orderService.getOrdersByUser(PageRequest.of(page, size)));
     }


    @GetMapping("/{orderId}/pay")
     public ResponseEntity<?> cancelOrder(@RequestParam Long orderId) throws BadRequestException {
          return ResponseEntity.ok(orderService.cancelOrder(orderId));
     }

     @GetMapping("/users/orders")
     public ResponseEntity<?> getOrderById(@RequestParam Long orderId) {

          return ResponseEntity.ok(orderService.getOrderById(orderId));

     }


}