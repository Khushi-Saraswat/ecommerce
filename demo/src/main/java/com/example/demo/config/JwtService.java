package com.example.demo.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.service.impl.UserInfoService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//jwt class is responsible for handling JWT (JSON Web Token) operations
// such as token generation, extraction of claims, and token validation.
@Component
public class JwtService {

    private final UserInfoService userInfoService;

    // Plain text secret (NOT Base64)
    public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";

    JwtService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    // Generate JWT token
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userInfoService.loadUserByUsername(email).getAuthorities());
        System.out.println("claims in jwtservice" + claims);
        return createToken(claims, email);
    }

    // Create JWT token (valid for 30 minutes)
    private String createToken(Map<String, Object> claims, String email) {

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey())
                .compact();
    }

    // CORRECT signing key
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Extract username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim extractor
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        System.out.println(claims + "claims");
        return claimResolver.apply(claims);
    }

    // Parse JWT
    private Claims extractAllClaims(String token) {
        token = token.trim();
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Check expiration
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
