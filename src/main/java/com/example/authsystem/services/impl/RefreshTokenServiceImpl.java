package com.example.authsystem.services.impl;

import com.example.authsystem.entity.RefreshToken;
import com.example.authsystem.exception.ApiException;
import com.example.authsystem.repository.RefreshTokenRepository;
import com.example.authsystem.security.JwtService;
import com.example.authsystem.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    private final long REFRESH_EXPIRATION = 7L * 24 * 60 * 60; // 7 days in seconds


    @Override
    public RefreshToken createRefreshToken(String email) {

        // One user -> one active refresh token (simple approach)
        refreshTokenRepository.deleteByUserEmail(email);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userEmail(email)
                .expiryDate(Instant.now().plusSeconds(REFRESH_EXPIRATION))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public String refreshAccessToken(String refreshToken) {

        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApiException("Invalid refresh token"));

        if (tokenEntity.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new ApiException("Refresh token expired, please login again");
        }

        return jwtService.generateToken(tokenEntity.getUserEmail());
    }

    @Override
    public void logout(String refreshToken) {

        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApiException("Invalid refresh token"));

        refreshTokenRepository.delete(tokenEntity);

    }
}
