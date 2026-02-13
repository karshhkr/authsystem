package com.example.authsystem.service;
import com.example.authsystem.dto.*;
import com.example.authsystem.entity.User;

import java.util.List;
public interface UserService {

    // USER APIs
    UserResponse getCurrentUser(String email);
    UserResponse updateProfile(String email, UpdateProfileRequest request);
    void changePassword(String email, ChangePasswordRequest request);

    // AUTH
    UserResponse registerUser(RegisterRequest request);
    AuthResponse login(LoginRequest request);

    // ADMIN (phase-2)
    UserPageResponse adminGetUsers(int page, int size, String search, String role, Boolean deleted);
    UserResponse adminGetUserById(Long id);
    void adminSoftDeleteUser(Long id);
    void adminRestoreUser(Long id);

}
