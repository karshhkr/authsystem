package com.example.authsystem.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserResponse user
) {

}
