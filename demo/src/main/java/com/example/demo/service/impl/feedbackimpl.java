package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.request.FeedBack.*;
import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.exception.Auth.AuthException;
import com.example.demo.model.Feedback;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.Feedbackrepo;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.FeedbackService;

@Service
public class feedbackimpl implements FeedbackService {

   @Autowired
   private Feedbackrepo feedbackrepo;

   @Autowired
   private ModelMapper modelMapper;

   @Autowired
   private ProductRepository productRepository;

   @Autowired
   private AuthService authService;

   @Override
   public FeedbackDto saveFeedBack(FeedbackDto feed) {

      User user = authService.getCurrentUser();
      if (user == null) {
            throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
      }
      
      Product product = productRepository.findById(feed.getProductId())
      .orElseThrow(() -> new RuntimeException("Product not found with id: " + feed.getProductId()));
      Feedback feedback=convertDtoToEntity(feed);
      feedback.setProduct(product);
      Feedback feedback2 = feedbackrepo.save(feedback);
      return convertEntityToDto(feedback2);
   }

   @Override
   public List<FeedbackDto> getFeedbackByProductId(Integer productId) {
      User user = authService.getCurrentUser();
      if (user == null) {
            throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
      }
       List<Feedback> feedbackdto=feedbackrepo.findByProductId(productId);
       // this will convert List of feedback into list of dto
       return feedbackdto.stream().map(feedback -> modelMapper.map(feedback,FeedbackDto.class))
       .collect(Collectors.toList());
   }

   @Override
   public List<FeedbackDto> getAllFeedBack(){
           User user = authService.getCurrentUser();
           if (user == null) {
            throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
           }
          List<Feedback>feedbackDtos=feedbackrepo.findAll();
            // this will convert List of feedback into list of dto
          return feedbackDtos.stream().map(feedback -> modelMapper.map(feedback,FeedbackDto.class))
          .collect(Collectors.toList());


   }

   public FeedbackDto convertEntityToDto(Feedback feedback) {
      return modelMapper.map(feedback, FeedbackDto.class);
   }

   public Feedback convertDtoToEntity(FeedbackDto feedbackDto) {
      return modelMapper.map(feedbackDto, Feedback.class);
   }

   

}
