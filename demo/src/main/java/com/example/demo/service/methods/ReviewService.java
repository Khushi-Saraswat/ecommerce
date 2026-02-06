package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.request.ReviewRequestDTO;
import com.example.demo.response.ReviewResponseDTO;

public interface ReviewService {

    ReviewResponseDTO saveReview(ReviewRequestDTO request);

    List<ReviewResponseDTO> getReviewsByProductId(Integer productId);

}
