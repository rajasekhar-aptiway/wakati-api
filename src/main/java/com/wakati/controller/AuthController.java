package com.wakati.controller;

import com.wakati.model.request.LoginRequest;
import com.wakati.model.request.OtpVerifyRequest;
import com.wakati.service.AuthService;
import com.wakati.service.OtpVerificationService;
import com.wakati.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OtpVerificationService service;

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req,
                                   HttpServletRequest request) {
        return ResponseEntity.ok(authService.login(req, request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> req,
                                     HttpServletRequest request) {
        String token = req.get("refresh_token");
        return ResponseEntity.ok(refreshTokenService.refresh(token, request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
        return ResponseEntity.ok(service.verifyOtp(request));
    }
}