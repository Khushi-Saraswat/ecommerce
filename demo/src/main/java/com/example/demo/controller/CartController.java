package com.example.demo.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.Cart.CartItemRequestDTO;
import com.example.demo.request.Cart.UpdateCartRequest;
import com.example.demo.service.methods.CartService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasAuthority('USER')")
public class CartController {

  @Autowired
  private CartService cartService;

  // Add product to cart - pass productId and quantity
  @PostMapping("/addCart")
  public ResponseEntity<?> addToCart(
      @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {

    return ResponseEntity.ok(cartService.saveCart(cartItemRequestDTO));

  }

  // Get all cart items for user
  @GetMapping("/getCart")
  public ResponseEntity<?> getCart() {

    return ResponseEntity.ok(cartService.getCartByUsers());

  }

  // Delete specific product from cart
  @DeleteMapping("/deleteCart/{productId}")
  public ResponseEntity<?> DeleteCart(@PathVariable Integer productId) throws BadRequestException {

    return ResponseEntity.ok(cartService.deleteCart(productId));
  }

  // Delete specific product from cart
  @PutMapping("/update/{productId}")
  public ResponseEntity<String> UpdateCart(@RequestBody UpdateCartRequest updateCartRequest)
      throws BadRequestException {

    return ResponseEntity.ok(cartService.updateQuantity(updateCartRequest));

  }
}
