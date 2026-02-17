package com.example.authsystem.controller;

import com.example.authsystem.dto.ChangePasswordRequest;
import com.example.authsystem.dto.UpdateProfileRequest;
import com.example.authsystem.dto.UserResponse;
import com.example.authsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    // 🔹 1. GET logged-in user profile
    @GetMapping("/me")
    public UserResponse getMyProfile(Authentication authentication) {

        String email = authentication.getName();
        return userService.getCurrentUser(email);
    }

    // 🔹 2. UPDATE logged-in user profile
    @PutMapping("/me")
    public UserResponse updateMyProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request
    ) {

        String email = authentication.getName();
        return userService.updateProfile(email, request);
    }

    // 🔹 3. CHANGE PASSWORD
    @PutMapping("/change-password")
    public String changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        userService.changePassword(authentication.getName(), request);
        return "Password changed successfully";
    }
}
