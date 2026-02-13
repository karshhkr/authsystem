package com.example.authsystem.service;

import com.example.authsystem.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String email);

    String refreshAccessToken(String refreshToken);

    void logout(String refreshToken);
}
