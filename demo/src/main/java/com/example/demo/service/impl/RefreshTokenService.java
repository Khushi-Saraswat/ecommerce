package com.example.demo.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.repository.UserRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;

    private final UserRepository userRepository;

    @Value("${refresh.token_expiry}")
    private Long refreshTokenDurationMs;

    public RefreshTokenService(RefreshTokenRepo refreshTokenRepo, UserRepository userRepository) {

        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(Long userId) {

        RefreshToken existingToken = refreshTokenRepo.findByUser_UserId(userId);

        if (existingToken != null) {
            // 🔁 Update existing refresh token
            existingToken.setExpiryDate(
                    Instant.now().plusMillis(refreshTokenDurationMs));
            existingToken.setToken(UUID.randomUUID().toString());

            return refreshTokenRepo.save(existingToken);
        }

        // 🆕 Create new refresh token if not exists
        RefreshToken newToken = new RefreshToken();
        newToken.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        newToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDurationMs));
        newToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepo.save(newToken);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

}
