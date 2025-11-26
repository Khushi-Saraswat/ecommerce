package com.example.demo.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.repository.UserRepository;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepo refreshTokenRepo;

    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepo refreshTokenRepo, UserRepository userRepository) {

        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(Long userId) {
        var token = new RefreshToken();
        token.setUser(userRepository.findById(userId).get());
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepo.save(token);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

}
