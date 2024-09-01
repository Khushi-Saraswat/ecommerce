package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Feedback;

public interface FeedbackService {

     public Boolean saveFeedBack(Feedback feedback);

     public List<Feedback> getAllFeedBack();
}
