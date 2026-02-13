package com.example.authsystem.services.impl;

import com.example.authsystem.dto.*;
import com.example.authsystem.entity.RefreshToken;
import com.example.authsystem.entity.User;
import com.example.authsystem.exception.ApiException;
import com.example.authsystem.repository.UserRepository;
import com.example.authsystem.security.JwtService;
import com.example.authsystem.service.RefreshTokenService;
import com.example.authsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new ApiException("User not found"));
        return mapToResponse(user);
    }

    @Override
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new ApiException("User not found"));

        user.setName(request.getName());
        userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new ApiException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new ApiException("Old password incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .deleted(false)
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid password");
        }

        String accessToken = jwtService.generateToken(user.getEmail());

        // ✅ refresh token create
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        UserResponse userResponse = mapToResponse(user);

        return new AuthResponse(accessToken, refreshToken.getToken(), userResponse);
    }

    // ===================== ✅ ADMIN METHODS =====================

    @Override
    public UserPageResponse adminGetUsers(int page, int size, String search, String role, Boolean deleted) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> usersPage = userRepository.searchUsers(
                (search == null || search.isBlank()) ? null : search,
                (role == null || role.isBlank()) ? null : role,
                deleted,
                pageable
        );

        List<UserResponse> users = usersPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new UserPageResponse(
                users,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.isLast()
        );
    }

    @Override
    public UserResponse adminGetUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found"));
        return mapToResponse(user);
    }

    @Override
    public void adminSoftDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found"));

        if (user.isDeleted()) return;

        user.setDeleted(true);
        user.setDeletedAt(Instant.now());
        userRepository.save(user);
    }

    @Override
    public void adminRestoreUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found"));

        user.setDeleted(false);
        user.setDeletedAt(null);
        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
