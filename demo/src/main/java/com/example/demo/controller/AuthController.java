package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.JwtService;
import com.example.demo.dto.UserDtlsDto;
import com.example.demo.exception.EmailNotSend;
import com.example.demo.exception.InvalidEmail;
import com.example.demo.exception.UserAlreadyExist;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.AuthRequest;
import com.example.demo.request.RegRequest;
import com.example.demo.response.AuthResponse;
import com.example.demo.service.impl.RefreshTokenService;
import com.example.demo.service.impl.UserInfoService;
import com.example.demo.service.impl.UserServiceImp;
import com.example.demo.service.methods.UserService;
import com.example.demo.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

// REST controller to handle authentication-related endpoints
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private final RefreshTokenService refreshTokenService;

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    AuthController(RefreshTokenService refreshTokenService, RefreshTokenRepo refreshTokenRepo) {
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    // create login endpoint- use to authenticate user if valid provide
    // authentication token.

    @PostMapping("/GenerateToken")
    public ResponseEntity<?> authenticationAndGetToken(@RequestBody AuthRequest authRequest) {

        AuthResponse authResponse = new AuthResponse();
        Authentication authentication;
        try {

            // authenticate() sends the username/password to the AuthenticationProvider,
            // which checks the raw password
            // against the stored hashed one. If they don't match, it throws an error.
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword()));

            UserDetails userDetails = userInfoService.loadUserByUsername(authRequest.getUsername());
            UserDtls userDtls = userRepository.findByusername(userDetails.getUsername());

            if (authentication.isAuthenticated()) {
                String jwt = jwtService.generateToken(userDetails.getUsername());
                authResponse.setJwt(jwt);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDtls.getId());

                return ResponseEntity.ok(Map.of(
                        "accessToken", jwt,
                        "refreshToken", refreshToken.getToken()));

            }
        }

        catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid username or password"));
        }

        catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Something went wrong on server"));
        }
        // Default return to satisfy compiler if authentication wasn't successful and
        // no exception was thrown. This could happen if
        // authentication.isAuthenticated()
        // returns false. Respond with 401 Unauthorized.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Authentication failed"));
    }

    // create register endpoint- use to register user.
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody RegRequest regrequest) {

        // check if username already exists
        UserDtls existingUser = userRepository.findByusername(regrequest.getName());
        if (existingUser != null) {
            throw new UserAlreadyExist("UserName is already exist !!");
        }

        // create new user entity dto from request
        UserDtlsDto userDtlsDto = new UserDtlsDto(regrequest.getName(), regrequest.getMobileNumber(),
                regrequest.getUsername(), regrequest.getPassword(), regrequest.getRole());
        // Save using service (which encodes password and initializes defaults)
        UserDtls savedUser = userServiceImp.saveUser(userDtlsDto);

        if (savedUser == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User registration failed due to server error.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully!");
    }

    // use refresh token to generate new jwt token
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {

        // extract refresh token from request body
        String requestToken = payload.get("refreshToken");
        // validate presence of refresh token
        return refreshTokenRepo.findByToken(requestToken)
                // if found, check if expired and generate new jwt token
                .map(token -> {
                    if (refreshTokenService.isTokenExpired(token)) {
                        refreshTokenRepo.delete(token);
                        return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
                    }
                    String newJwt = jwtService.generateToken(token.getUser().getUsername());
                    return ResponseEntity.ok(Map.of("token", newJwt));
                })
                // if not found, return error response
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));

    }

    // logout user by deleting refresh token
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");

        // validate presence of refresh token
        if (requestToken == null || requestToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        // find and delete the refresh token to log out the user
        return refreshTokenRepo.findByToken(requestToken)
                .map(token -> {
                    refreshTokenRepo.delete(token);
                    return ResponseEntity.ok("Logged out successfully");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token"));
    }

    // forgot password logic..
    @PostMapping("/forgot-password")
    public ResponseEntity<String> processForgotPassword(@RequestParam String username, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        // find user using entered email
        UserDtls userByEmail = userService.getUserByEmail(username);

        // if user is not found show error invalid email..
        if (ObjectUtils.isEmpty(userByEmail)) {
            throw new InvalidEmail("Invalid email !!!!");
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
                return ResponseEntity.status(HttpStatus.CREATED).body("please check the email password link is send");
            } else {

                throw new EmailNotSend("Something wrong on server ! Email not send");
            }

        }

    }

    // reset password logic..
    @PostMapping("/reset-password")
    public ResponseEntity<String> ResetPassword(@RequestParam String token, @RequestParam String password) {

        // get user by token
        UserDtls userByToken = userService.getUserByToken(token);
        if (userByToken == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The password reset link is invalid or expired.");

        } else {
            // update new password
            userByToken.setPassword(passwordEncoder.encode(password));
            // set token null after updating password
            userByToken.setResetToken(null);
            // save user
            userService.updateUser(userByToken);
            return ResponseEntity.status(HttpStatus.CREATED).body("password is set correctly");
        }

    }

}
