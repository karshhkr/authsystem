package com.example.authsystem.services.impl;

import com.example.authsystem.entity.RefreshToken;
import com.example.authsystem.entity.User;
import com.example.authsystem.exception.ApiException;
import com.example.authsystem.repository.RefreshTokenRepository;
import com.example.authsystem.repository.UserRepository;
import com.example.authsystem.security.JwtService;
import com.example.authsystem.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private static final long REFRESH_EXPIRATION = 7L * 24 * 60 * 60; // 7 days

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String email) {

        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new ApiException("User not found"));

        //  one user -> one active refresh token
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusSeconds(REFRESH_EXPIRATION))
                .revoked(false) //  important (since column exists)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public String refreshAccessToken(String refreshToken) {

        //  join fetch user to avoid lazy session issue
        RefreshToken tokenEntity = refreshTokenRepository.findByTokenWithUser(refreshToken)
                .orElseThrow(() -> new ApiException("Invalid refresh token"));

        if (tokenEntity.isRevoked()) {
            throw new ApiException("Refresh token revoked, please login again");
        }

        if (tokenEntity.getExpiryDate().isBefore(Instant.now())) {
            tokenEntity.setRevoked(true);
            refreshTokenRepository.save(tokenEntity);
            throw new ApiException("Refresh token expired, please login again");
        }

        return jwtService.generateToken(tokenEntity.getUser().getEmail());
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {

        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApiException("Invalid refresh token"));

        //  revoke instead of delete (better + matches your DB column)
        tokenEntity.setRevoked(true);
        refreshTokenRepository.save(tokenEntity);
    }
}