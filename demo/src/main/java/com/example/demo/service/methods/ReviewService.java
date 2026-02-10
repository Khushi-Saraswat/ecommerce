package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.request.Review.ReviewRequestDTO;
import com.example.demo.response.Review.ReviewResponseDTO;

public interface ReviewService {

    ReviewResponseDTO saveReview(ReviewRequestDTO request);

    List<ReviewResponseDTO> getReviewsByProductId(Integer productId);

}
