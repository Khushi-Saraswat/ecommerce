package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.response.Wishlist.WishlistDto;
import com.example.demo.service.methods.WishlistService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/wishlist")
public class WishController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/create")
    public ResponseEntity<WishlistDto> createWishlist(@RequestBody User user) {
        return ResponseEntity.ok(wishlistService.createWishlist());
    }

    @GetMapping()
    public ResponseEntity<WishlistDto> getWishlistByUserId() {

        return ResponseEntity.ok(wishlistService.getWishlistByUserId());

    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishlistDto> addProductToWishlist(
            @PathVariable Integer productId) {
        return ResponseEntity.ok(wishlistService.addProductToWishlist(

                productId));
    }

}
