package com.wakati.service;

import com.wakati.config.JwtValidationResult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private Key key;

    private final String secret;
    private final String appEnv;

    public JwtService() {
        this.secret = System.getenv("JWT_SECRET");
        this.appEnv = System.getenv().getOrDefault("APP_ENV", "production");
    }

    @PostConstruct
    public void init() {

        String finalSecret = secret;

        if (finalSecret == null || finalSecret.isBlank()) {

            if ("development".equalsIgnoreCase(appEnv)) {
                finalSecret = "dev-secret-key-hurimoney-testing-only-not-for-prod";
                System.out.println("⚠️ WARNING: JWT_SECRET not set — using dev key");
            } else {
                throw new RuntimeException("JWT secret missing (production)");
            }
        }

        this.key = Keys.hmacShaKeyFor(finalSecret.getBytes());
    }

    // ✅ Generate JWT
    public String generateToken(Map<String, Object> payload, long expirySeconds) {

        Instant now = Instant.now();

        return Jwts.builder()
                .setClaims(payload)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirySeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .setId(UUID.randomUUID().toString())
                .setIssuer("wakati")
                .compact();
    }

    // ✅ Validate JWT
    public JwtValidationResult validateToken(String token) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return JwtValidationResult.valid(claims);

        } catch (ExpiredJwtException e) {
            return JwtValidationResult.invalid("Token expired");

        } catch (UnsupportedJwtException | MalformedJwtException e) {
            return JwtValidationResult.invalid("Invalid token format");

        } catch (SecurityException e) {
            return JwtValidationResult.invalid("Invalid signature");

        } catch (Exception e) {
            return JwtValidationResult.invalid("Token validation failed");
        }
    }
}