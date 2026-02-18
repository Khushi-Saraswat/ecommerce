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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.request.category.CategoryRequestDTO;
import com.example.demo.response.Product.ProductResponseDTO;
import com.example.demo.response.category.CategoryRequestResponseDTO;
import com.example.demo.response.category.CategoryResponseDTO;
import com.example.demo.service.methods.AdminService;
import com.example.demo.service.methods.CategoryRequestService;
import com.example.demo.service.methods.CategoryService;
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

  // <----- artisan management-------->
  @GetMapping("/artisans")
  public ResponseEntity<?> GetArtisan(@RequestHeader("Authorization") String jwt) {

    return ResponseEntity.ok(adminService.getAllArtisans(jwt));

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
  @GetMapping("/all")
  public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String jwt) {

    return ResponseEntity.ok(adminService.getAllUsers(jwt));

  }

  // block users
  @PatchMapping("/users/{id}/block")
  public ResponseEntity<?> blockUser(@PathVariable("id") Long userId,
      @RequestHeader("Authorization") String jwt) {

    return ResponseEntity.ok(adminService.blockUser(userId, jwt));

  }

  // get all orders
  @GetMapping("/users/orders")
  public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String jwt) {

    return ResponseEntity.ok(adminService.getAllOrders(jwt));

  }

  // get daily metrics-dashboard

  @GetMapping("/daily-metrics")
  public ResponseEntity<?> getDailyMetrics(@RequestHeader("Authorization") String jwt) {
    return ResponseEntity.ok(adminService.getDailyMetrics(jwt));
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

  // ✅ Delete Category
  @DeleteMapping("/categories/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long id) throws BadRequestException, Exception {
    return ResponseEntity.ok(categoryService.deleteCategory(id));
  }

  @GetMapping("/all")
  public ResponseEntity<Page<CategoryResponseDTO>> getAllCategory(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "12") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(categoryService.getAllCategory(pageable));
  }

  // category request management
  @GetMapping("/pending")
  public ResponseEntity<List<CategoryRequestResponseDTO>> getPendingRequests() {
    return ResponseEntity.ok(categoryRequestService.getPendingRequests());
  }

  // PUT /api/admin/category-requests/{requestId}/approve
  @PutMapping("/{requestId}/approve")
  public ResponseEntity<String> approveRequest(@PathVariable Long requestId) {
    return ResponseEntity.ok(categoryRequestService.approveRequest(requestId));
  }

  // PUT /api/admin/category-requests/{requestId}/reject?reason=...
  @PutMapping("/{requestId}/reject")
  public ResponseEntity<String> rejectRequest(
      @PathVariable Long requestId,
      @RequestParam(required = false) String reason) {
    return ResponseEntity.ok(categoryRequestService.rejectRequest(requestId, reason));
  }

  // product management........

  // this method is responsible for saving the product of a particular category-by
  // artian

  /*
   * @GetMapping("/getArtisanProduct")
   * public ResponseEntity<?> GetArtisanProduct(
   * 
   * @RequestHeader("Authorization") String jwt)
   * throws IOException {
   * 
   * return ResponseEntity.ok(productService.getByArtisanId(jwt));
   * 
   * }
   * 
   * // stock is updated-by admin
   * 
   * @PatchMapping("/StockUpdate/stock")
   * public ResponseEntity<String> UpdateStock(@RequestParam Integer
   * ProductId, @RequestParam Integer stock) {
   * String message = productService.IncreaseStock(ProductId, stock);
   * return ResponseEntity.ok(message);
   * }
   */

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

}
