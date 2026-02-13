package com.example.authsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
@Service
public class JwtService {

    // ✅ minimum 32 chars
    private static final String SECRET_KEY = "THIS_IS_A_DEMO_SECRET_KEY_32_CHARS!!";

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // 1) Generate token
    public String generateToken(String email) {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(exp)
                .id(UUID.randomUUID().toString().toString())
                .signWith(getKey())
                .compact();
    }

    // 2) Extract email
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 3) Token valid?
    public boolean isTokenValid(String token, String userEmail) {
        String extractedEmail = extractEmail(token);
        return extractedEmail.equals(userEmail) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
