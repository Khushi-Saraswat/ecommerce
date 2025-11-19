package com.example.demo.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FeedbackDto;
import com.example.demo.model.Feedback;
import com.example.demo.repository.Feedbackrepo;

@Service
public class feedbackimpl implements FeedbackService {

    @Autowired
    private Feedbackrepo feedbackrepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FeedbackDto saveFeedBack(FeedbackDto feed) {

       Feedback feedback=feedbackrepo.save(convertDtoToEntity(feed));
       return convertEntityToDto(feedback);
    
    }

  

    public FeedbackDto convertEntityToDto(Feedback feedback){
        return modelMapper.map(feedback,FeedbackDto.class);
    }

     public Feedback convertDtoToEntity(FeedbackDto feedbackDto){
        return modelMapper.map(feedbackDto,Feedback.class);
     }

     @Override
     public List<FeedbackDto> getFeedbackByProductId(Integer productId) {
                feedbackrepo.get
        return null;
     }

    
}
