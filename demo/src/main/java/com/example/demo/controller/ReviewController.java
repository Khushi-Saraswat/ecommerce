package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.request.Review.ReviewRequestDTO;
import com.example.demo.response.Review.ReviewResponseDTO;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AuthService authService;

    // 🔹 Get all reviews by Product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProductId(
            @PathVariable Integer productId) {

        List<ReviewResponseDTO> reviews = reviewService.getReviewsByProductId(productId);

        return ResponseEntity.ok(reviews);
    }

    // 🔹 Save a new review
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> saveReview(
            @Valid @RequestBody ReviewRequestDTO request) {

        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            request.setUserId(currentUser.getUserId());
        }

        ReviewResponseDTO response = reviewService.saveReview(request);

        return ResponseEntity.ok(response);
    }
}
