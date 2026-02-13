package com.example.authsystem.dto;

import java.util.List;

public record UserPageResponse(
        List<UserResponse> users,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
)
{
}
