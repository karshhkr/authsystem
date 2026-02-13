package com.example.authsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;
import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder   // 🔥 THIS IS REQUIRED
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    // ✅ Soft delete
    @Builder.Default
    private boolean deleted = false;

    private Instant deletedAt;


}
