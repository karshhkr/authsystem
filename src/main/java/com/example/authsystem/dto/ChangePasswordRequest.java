package com.example.authsystem.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {}
