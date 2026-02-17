package com.example.authsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Old password required")
        String oldPassword,

        @Size(min = 6, message = "New password must be at least 6 characters")
        String newPassword
) {}
