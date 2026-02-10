package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.response.Cart.CartResponse;
import com.example.demo.service.methods.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  @Autowired
  private CartService cartService;

  // Add product to cart - pass productId and quantity
  @PostMapping("/addCart")
  public ResponseEntity<?> addToCart(
      @RequestHeader("Authorization") String token,
      @RequestParam Integer pid,
      @RequestParam int quantity) {

    try {
      CartResponse response = cartService.saveCart(token, pid, quantity);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
  }

  // Get all cart items for user
  @GetMapping("/getCart")
  public ResponseEntity<?> getCart(@RequestHeader("Authorization") String token) {

    try {
      return ResponseEntity.ok(cartService.getCartByUsers(token));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
  }

  // Delete specific product from cart
  @DeleteMapping("/deleteCart/{productId}")
  public ResponseEntity<?> DeleteCart(@PathVariable Integer productId) {

    try {
      Boolean result = cartService.deleteCart(productId);
      if (result) {
        return ResponseEntity.ok("Product removed from cart successfully");
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Product not found in cart");
      }
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
  }

  // Inner class for error response
  public static class ErrorResponse {
    private String message;
    private int status;

    public ErrorResponse(String message, int status) {
      this.message = message;
      this.status = status;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }
  }
}
