package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.example.demo.response.Others.ApiResponse;
import com.example.demo.service.methods.AdminService;
import com.example.demo.service.methods.CategoryService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  @Autowired
  private AdminService adminService;

  @Autowired
  private CategoryService categoryService;

  @GetMapping("/artisans")
  public ResponseEntity<?> GetArtisan(@RequestHeader("Authorization") String jwt) {

    return ResponseEntity.ok(adminService.getAllArtisans(jwt));

  }

  // here id is artisan id
  @PatchMapping("/artisans/{id}/approve")
  public ResponseEntity<String> ApproveArtisan(@PathVariable("id") Long artisanId,
      @RequestHeader("Authorization") String jwt) {

    return ResponseEntity.ok(adminService.approveArtisan(artisanId, jwt));

  }

  // here id is artisan id
  @PatchMapping("/artisans/{id}/reject")
  public ResponseEntity<?> RejectArtisan(@PathVariable("id") Long artisanId,
      @RequestHeader("Authorization") String jwt) {

    return ResponseEntity.ok(adminService.rejectArtisan(artisanId, jwt));

  }

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

  // get daily metrics
  /*
   * @GetMapping("/daily-metrics")
   * public ResponseEntity<?> getDailyMetrics(@RequestHeader("Authorization")
   * String jwt) {
   * return ResponseEntity.ok(adminService.getDailyMetrics(jwt));
   * }
   * 
   * //
   * -------------------------------Categories------------------------------------
   * ----------//
   * 
   * @PostMapping("/categories")
   * public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(
   * 
   * @Valid @RequestBody CategoryRequestDTO request) {
   * CategoryResponseDTO category = categoryService.createCategory(request);
   * return ResponseEntity.ok(ApiResponse.success("Category created successfully",
   * category));
   * }
   * 
   * @PutMapping("/categories/{id}")
   * public ResponseEntity<<CategoryResponseDTO>> updateCategory(
   * 
   * @PathVariable String id,
   * 
   * @RequestBody CategoryRequestDTO request) {
   * CategoryResponseDTO category = categoryService.updateCategory(id, request);
   * return ResponseEntity.ok(ApiResponse.success("Category updated successfully",
   * category));
   * }
   * 
   * @DeleteMapping("/categories/{id}")
   * public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String
   * id) {
   * categoryService.deleteCategory(id);
   * return ResponseEntity.ok(ApiResponse.success("Category deleted successfully",
   * null));
   * }
   */

}
