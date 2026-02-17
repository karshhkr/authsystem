package com.example.authsystem.controller;

import com.example.authsystem.dto.*;
import com.example.authsystem.service.RefreshTokenService;
import com.example.authsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = refreshTokenService.refreshAccessToken(request.refreshToken());
        return new RefreshTokenResponse(newAccessToken);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody LogoutRequest request) {
        refreshTokenService.logout(request.refreshToken());
        return "Logged out successfully";
    }
}
