package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Feedback;
import com.example.demo.repository.Feedbackrepo;

@Service
public class feedbackimpl implements FeedbackService {

    @Autowired
    private Feedbackrepo feedbackrepo;

    @Override
    public Boolean saveFeedBack(Feedback feedback) {
        feedbackrepo.save(feedback);
        return true;
    }

    @Override
    public List<Feedback> getAllFeedBack() {
        List<Feedback> feedback = feedbackrepo.findAll();
        return feedback;
    }
}
