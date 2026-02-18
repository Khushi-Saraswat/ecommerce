package com.example.demo.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.config.JwtService;
import com.example.demo.constants.Role;
import com.example.demo.constants.TokenPair;
import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.Auth.AuthException;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.Auth.AuthRequest;
import com.example.demo.request.User.UserRequestDTO;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.UserService;
import com.example.demo.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public TokenPair login(AuthRequest authRequest) {

        if (authRequest == null || authRequest.getUsername() == null || authRequest.getPassword() == null) {
            throw new AuthException("Empty details", AuthErrorType.EMPTY_CREDENTIALS);
        }

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthException("Invalid credentials", AuthErrorType.INVALID_CREDENTIALS);
        } catch (AuthenticationException e) {
            throw new AuthException("authentication failed", AuthErrorType.AUTHENTICATION_FAILED);
        }

        if (!authentication.isAuthenticated()) {
            throw new AuthException("Authentication Failed", AuthErrorType.AUTHENTICATION_FAILED);
        }

        // ✅ IMPORTANT: principal from authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByusername(userDetails.getUsername());
        if (user == null) {
            throw new UserException("User not found", UserErrorType.NOT_FOUND);
        }

        // Generate JWT token
        String jwt = jwtService.generateToken(userDetails.getUsername());

        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserId());

        TokenPair tokenPair = new TokenPair();
        tokenPair.setJwt(jwt);
        tokenPair.setRefreshToken(refreshToken);

        return tokenPair;
    }

    @Override
    public String registerUser(UserRequestDTO userDtlsDto) {
        User user = (User) abstractMapperService.toEntity(userDtlsDto, User.class);

        User existingUser = userRepository.findByusername(user.getName());

        if (existingUser != null) {
            throw new UserException("user already exist", UserErrorType.USERNAME_TAKEN);
        }

        System.out.println("user roles" + user);
        System.out.println(" before user roles" + user.getRole());
        if (user.getRole().equals(Role.CUSTOMER)) {

            user.setRole(Role.CUSTOMER);

        } else if (user.getRole().equals(Role.ARTISAN)) {

            user.setRole(Role.ARTISAN);

        } else {
            user.setRole(Role.ADMIN);
        }
        System.out.println(" after user roles" + user.getRole());

        // Encode password (important to always encode before saving)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setName(user.getName());
        user.setMobileNumber(user.getMobileNumber());
        User saveUser = userRepository.save(user);

        if (saveUser == null) {
            throw new UserException("user registration failed due to server error", UserErrorType.REGISTRATION_FAILED);
        }

        return "User is saved successfully !!";

    }

    @Override
    public String refreshToken(Map<String, String> payload) {

        // Extract refresh token from request body
        String requestToken = payload.get("refreshToken");

        // Validate that refresh token is present
        if (requestToken == null || requestToken.trim().isEmpty()) {
            throw new AuthException("refresh token is required", AuthErrorType.REFRESH_TOKEN_REQUIRED);
        }

        // Find and validate refresh token
        return refreshTokenRepo.findByToken(requestToken)
                .map(token -> {
                    // Check if token is expired
                    if (refreshTokenService.isTokenExpired(token)) {
                        refreshTokenRepo.delete(token);
                        throw new AuthException("Refresh token has expired", AuthErrorType.REFRESH_TOKEN_EXPIRED);
                    }

                    // Generate new JWT token
                    String newJwt = jwtService.generateToken(token.getUser().getUsername());
                    return newJwt;
                })
                .orElseThrow(() -> new AuthException("Invalid refresh token", AuthErrorType.REFRESH_TOKEN_INVALID));
    }

    @Override
    public String logout(Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");

        // Validate presence of refresh token
        if (requestToken == null || requestToken.isBlank()) {
            throw new AuthException("refresh token is required", AuthErrorType.REFRESH_TOKEN_REQUIRED);
        }

        // Find and delete the refresh token to log out the user
        return refreshTokenRepo.findByToken(requestToken)
                .map(token -> {
                    refreshTokenRepo.delete(token);
                    return "Logged out successfully";
                })
                .orElseThrow(() -> new AuthException("Invalid refresh token", AuthErrorType.REFRESH_TOKEN_INVALID));

    }

    @Override
    public String processForgotPassword(String username, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        // find user using entered email
        User userByEmail = userService.getUserByEmail(username);
        System.out.println("request" + "request");

        // if user is not found show error invalid email..
        if (ObjectUtils.isEmpty(userByEmail)) {
            throw new UserException("Invalid email", UserErrorType.INVALID_EMAIL);
        }
        // if user is found create token and add it to the end of generated email and
        // send email via passing url and email.
        else {

            // generate random 36-character string token for reset password
            String resetToken = UUID.randomUUID().toString();
            // save token to the database
            userService.updateUserResetToken(username, resetToken);
            // generate-url-http://localhost:8080/reset-password?tokendhegffffffff
            String url = commonUtil.generateUrl(request) + "/reset-password?token=" +
                    resetToken;
            // send email to user
            Boolean sendMail = commonUtil.sendMail(url, username);
            if (sendMail) {
                // if email is sent successfully show message to check email.
                return "please check the email password link is send";
            } else {
                throw new RuntimeException("something wrong on server ! Email is not send");
            }

        }

    }

    @Override
    public String ResetPassword(String token, String password) {
        // get user by token
        User userByToken = userService.getUserByToken(token);
        if (userByToken == null) {

            throw new UserException("password reset link is invalid", UserErrorType.PASSWORD_LINKFAILED);

        } else {
            // update new password
            userByToken.setPassword(passwordEncoder.encode(password));
            // set token null after updating password
            userByToken.setResetToken(null);
            // save user
            userService.updateUser(userByToken);
            return "password is set correctly";
        }
    }

    @Override
    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication + "authentication");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // getUsername() - Returns the username used to authenticate the user.
        System.out.println("User name: " + userDetails.getUsername());

        // getAuthorities() - Returns the authorities granted to the user.
        System.out.println("User has authorities: " + userDetails.getAuthorities());

        User user = userRepository.findByusername(userDetails.getUsername());
        System.out.println(user + "user");
        return user;

    }
}
