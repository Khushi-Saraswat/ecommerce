package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.Auth.AuthRequest;
import com.example.demo.request.User.UserRequestDTO;
import com.example.demo.service.methods.AuthService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

// this controller is responsible for authentication and authorization
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // create login endpoint- use to authenticate user if valid provide
    // authentication token.

    @PostMapping("/login")
    public ResponseEntity<?> authenticationAndGetToken(@RequestBody AuthRequest authRequest) {

        return ResponseEntity.ok(authService.login(authRequest));

    }

    // create register endpoint- use to register user.
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody UserRequestDTO regrequest) {

        return ResponseEntity.ok(authService.registerUser(regrequest));

    }

    // use refresh token to generate new jwt token if first one is expired
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {

        return ResponseEntity.ok(authService.refreshToken(payload));

    }

    // logout user by deleting refresh token
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {

        return ResponseEntity.ok(authService.logout(payload));
    }

    // forgot password logic..
    @PostMapping("/forgot-password")
    public ResponseEntity<String> processForgotPassword(@RequestParam String username, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        return ResponseEntity.ok(authService.processForgotPassword(username, request));

    }

    // reset password logic..
    @PostMapping("/reset-password")
    public ResponseEntity<String> ResetPassword(@RequestParam String token, @RequestParam String password) {

        return ResponseEntity.ok(authService.ResetPassword(token, password));

    }

}
