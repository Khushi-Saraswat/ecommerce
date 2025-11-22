package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.dto.FeedbackDto;

public interface FeedbackService {

     public FeedbackDto saveFeedBack(FeedbackDto feedback);

     public List<FeedbackDto> getFeedbackByProductId(Integer productId);
}
