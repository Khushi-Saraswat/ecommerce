package com.example.demo.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;

//jwt class is responsible for handling JWT (JSON Web Token) operations
// such as token generation, extraction of claims, and token validation.

@Component
public class JwtService {

    public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";

    // responsible for creating a new jwt token when a user successfully logs
    // into the applicationIt takes user info (username, ID, roles), adds them as
    // claims in the token payload, signs the token with a secret/private key using
    // an algorithm
    // like HS256/RS256, and includes exp and iat to control its validity period.
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    // Build jwt tokens with claims, subject, issued time, expiration time, and
    // signing algorithm
    // Token valid for 3 minutes
    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
                .signWith(getSignKey())
                .compact();

    }

    // creates a signing key from the base64 encoded secret
    // returns a Key Object for signing the jwt
    public javax.crypto.SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    // Extracts the userName from the jwt token.
    // username contained in the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts the expiration date from the jwt token
    // return - the userName contained in the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // extracts a specific claim from the jwt token.
    // claimResolver A function to extract the claim.
    // return the value of specified claim
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extracts all claims from the JWT token.
    // return-> Claims object containing all claims.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Checks if the JWT token is expired.
    // return-> True if the token is expired, false otherwise.
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validates the JWT token against the UserDetails.
    // return-> True if the token is valid, false otherwise.//Validates the JWT
    // token against the UserDetails.
    // return-> True if the token is valid, false otherwise.
    public Boolean validateToken(String token, UserDetails userDetails) {

        final String userName = extractUsername(token);

        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

}
