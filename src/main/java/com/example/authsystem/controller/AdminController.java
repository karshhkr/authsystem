package com.example.authsystem.controller;

import com.example.authsystem.dto.UserPageResponse;
import com.example.authsystem.dto.UserResponse;
import com.example.authsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    //  old test endpoint
    @GetMapping("/hello")
    public String helloAdmin() {
        return "Hello Admin";
    }

    // 1) Get users (pagination + search + filters)
    // GET /api/admin/users=false
    @GetMapping("/users")
    public UserPageResponse getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean deleted
    ) {
        return userService.adminGetUsers(page, size, search, role, deleted);
    }

    // ✅ 2) Get user by id
    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.adminGetUserById(id);
    }

    // ✅ 3) Soft delete user
    @DeleteMapping("/users/{id}")
    public String softDelete(@PathVariable Long id) {
        userService.adminSoftDeleteUser(id);
        return "User soft deleted";
    }

    // ✅ 4) Restore user
    @PutMapping("/users/{id}/restore")
    public String restore(@PathVariable Long id) {
        userService.adminRestoreUser(id);
        return "User restored";
    }
}
