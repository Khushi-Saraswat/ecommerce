package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.request.Review.ReviewRequestDTO;
import com.example.demo.response.Review.ReviewResponseDTO;
import com.example.demo.service.methods.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

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

        Review review = abstractMapperService.toEntity(request, Review.class);
        return abstractMapperService.toDto(reviewRepository.save(review), ReviewResponseDTO.class);
    }

}
