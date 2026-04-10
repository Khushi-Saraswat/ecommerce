package com.example.demo.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.request.category.CategoryRequestDTO;
import com.example.demo.response.Product.DeleteProductResponseDTO;
import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.response.category.CategoryRequestResponseDTO;
import com.example.demo.response.category.CategoryResponseDTO;
import com.example.demo.service.methods.AdminService;
import com.example.demo.service.methods.CategoryRequestService;
import com.example.demo.service.methods.CategoryService;
import com.example.demo.service.methods.OrderService;
import com.example.demo.service.methods.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  @Autowired
  private AdminService adminService;

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private CategoryRequestService categoryRequestService;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private ProductService productService;

  @Autowired
  private OrderService orderService;

  // <----- artisan management-------->
  @GetMapping("/artisans")
  public ResponseEntity<?> GetArtisan() {

    return ResponseEntity.ok(adminService.getAllArtisans());

  }

  // here id is artisan id
  @PatchMapping("/artisans/{id}/approve")
  public ResponseEntity<String> ApproveArtisan(@PathVariable("id") Long artisanId) {

    return ResponseEntity.ok(adminService.approveArtisan(artisanId));

  }

  // here id is artisan id
  @PatchMapping("/artisans/{id}/reject")
  public ResponseEntity<?> RejectArtisan(@PathVariable("id") Long artisanId) {

    return ResponseEntity.ok(adminService.rejectArtisan(artisanId));

  }

  // <----- user management -------->
  // get all user -(customer admin artisan)
  @GetMapping("/users")
  public ResponseEntity<?> getAllUsers() {

    return ResponseEntity.ok(adminService.getAllUsers());

  }

  // block users
  @PatchMapping("/users/{id}/block")
  public ResponseEntity<?> blockUser(@PathVariable("id") Long userId
      ) {

    return ResponseEntity.ok(adminService.blockUser(userId));

  }

  // orders management--
  // get all orders
  @GetMapping("/orders")
  public ResponseEntity<?> getAllOrders() {

    return ResponseEntity.ok(adminService.getAllOrders());

  }

  @PutMapping("/orders/{id}/status")
  public ResponseEntity<?> UpdateOrderStatus(@PathVariable("id") Long orderId,
      @RequestParam String status) {

    return ResponseEntity.ok(orderService.updatOrderStatus(orderId, status));
  }

  // dashboard management

  @GetMapping("/daily-metrics")
  public ResponseEntity<?> getDailyMetrics() {
    return ResponseEntity.ok(adminService.getDailyMetrics());
  }

  // category management

  // ✅ Create Category (Multipart)
  @PostMapping(value = "/categories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> createCategory(
      @RequestPart("category") String categoryJson,
      @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

    CategoryRequestDTO request = mapper.readValue(categoryJson, CategoryRequestDTO.class);

    return ResponseEntity.ok(categoryService.createCategory(request, file));
  }

  // ✅ Update Category (Multipart)
  @PutMapping(value = "/categories/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> updateCategory(
      @PathVariable Long id,
      @RequestPart("category") String categoryJson,
      @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException, Exception {

    CategoryRequestDTO request = mapper.readValue(categoryJson, CategoryRequestDTO.class);

    return ResponseEntity.ok(categoryService.updateCategory(id, request, file));
  }

  @DeleteMapping("/categories/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long id) throws BadRequestException, Exception {
    return ResponseEntity.ok(categoryService.deleteCategory(id));
  }

  @PreAuthorize("hasRole('ADMIN','ARTISAN')")
  @GetMapping("/categories")
  public ResponseEntity<Page<CategoryResponseDTO>> getAllCategory(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "12") int size) {

    Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(categoryService.getAllCategory(pageable));
  }

  // category request management
  @GetMapping("/category-requests/pending")
  public ResponseEntity<List<CategoryRequestResponseDTO>> getPendingRequests() {
    return ResponseEntity.ok(categoryRequestService.getPendingRequests());
  }

  // PUT /api/admin/category-requests/{requestId}/approve
  @PutMapping("/category-requests/{requestId}/approve")
  public ResponseEntity<String> approveRequest(@PathVariable Long requestId) {
    return ResponseEntity.ok(categoryRequestService.approveRequest(requestId));
  }

  // PUT /api/admin/category-requests/{requestId}/reject?reason=...
  @PutMapping("/category-requests/{requestId}/reject")
  public ResponseEntity<String> rejectRequest(
      @PathVariable Long requestId,
      @RequestParam(required = false) String reason) {
    return ResponseEntity.ok(categoryRequestService.rejectRequest(requestId, reason));
  }

  // product management........
  @GetMapping("/products")
  public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "12") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDir

  ) {
    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<ProductResponseDTO> products = productService.getAllProducts(pageable);
    return ResponseEntity.ok(products);
  }

  @GetMapping("/products/{id}")
  public ProductResponseDTO getById(@PathVariable Integer id) {
    return productService.getProductById(id);
  }

  @PutMapping("/products/{id}/status")
  public DeleteProductResponseDTO updateStatus(
      @PathVariable Integer id,
      @RequestParam Boolean active) {
    return productService.toggleProductStatusByAdmin(id, active);
  }

  @DeleteMapping("/products/{id}")
  public DeleteProductResponseDTO hardDelete(@PathVariable Integer id) {
    return productService.DeactivateProduct(id);
  }

}
