package com.example.demo.service.methods;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.example.demo.constants.TokenPair;
import com.example.demo.model.User;
import com.example.demo.request.AuthRequest;
import com.example.demo.request.UserRequestDTO;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    public String registerUser(UserRequestDTO userDtlsDto);

    public TokenPair login(AuthRequest authRequest);

    public String refreshToken(Map<String, String> payload);

    public String logout(Map<String, String> payload);

    public String processForgotPassword(String username, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException;

    public String ResetPassword(String token, String password);

    public User getCurrentUser();

}
