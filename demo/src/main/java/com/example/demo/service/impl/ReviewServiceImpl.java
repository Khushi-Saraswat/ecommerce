package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.errorTypes.ProductErrorType;
import com.example.demo.constants.errorTypes.ReviewErrorType;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.Product.ProductException;
import com.example.demo.exception.Review.ReviewException;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.Review.ReviewRequestDTO;
import com.example.demo.response.Review.ReviewResponseDTO;
import com.example.demo.service.methods.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Override
    public List<ReviewResponseDTO> getReviewsByProductId(Integer productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream()
                .map(review -> abstractMapperService.toDto(review, ReviewResponseDTO.class))
                .toList();
    }

    @Override
    public ReviewResponseDTO saveReview(ReviewRequestDTO request) {
        // Validate rating
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new ReviewException("Rating must be between 1 and 5", ReviewErrorType.INVALID_RATING);
        }

        // Validate comment
        if (request.getComment() == null || request.getComment().trim().isEmpty()) {
            throw new ReviewException("Comment cannot be empty", ReviewErrorType.INVALID_COMMENT);
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException("User not found", UserErrorType.NOT_FOUND));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException("Product not found", ProductErrorType.PRODUCT_NOT_FOUND));

        // Check for duplicate review
        Optional<Review> existingReview = reviewRepository.findByUserIdAndProductId(
                request.getUserId(), request.getProductId());
        if (existingReview.isPresent()) {
            throw new ReviewException("User has already reviewed this product", ReviewErrorType.DUPLICATE_REVIEW);
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        if (savedReview == null) {
            throw new ReviewException("Failed to save review", ReviewErrorType.REVIEW_NOT_SAVED);
        }

        return abstractMapperService.toDto(savedReview, ReviewResponseDTO.class);
    }

}
