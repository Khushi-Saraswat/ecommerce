package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.FeedbackDto;
import com.example.demo.model.Feedback;

public interface FeedbackService {

     public FeedbackDto saveFeedBack(FeedbackDto feedback);

     public List<FeedbackDto> getFeedbackByProductId(Integer productId);
}
