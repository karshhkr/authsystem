package com.example.authsystem.repository;

import com.example.authsystem.entity.RefreshToken;
import com.example.authsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    // 1 user ke multiple tokens ho sakte hain => List
    List<RefreshToken> findAllByUser(User user);

    // user ke saare tokens delete
    void deleteByUser(User user);

    // agar direct userId se delete karna ho
    void deleteAllByUser_Id(Long userId);

    boolean existsByToken(String token);
    // ✅ fix for "Could not initialize proxy ... no session"
    @Query("SELECT rt FROM RefreshToken rt JOIN FETCH rt.user WHERE rt.token = :token")
    Optional<RefreshToken> findByTokenWithUser(@Param("token") String token);
}