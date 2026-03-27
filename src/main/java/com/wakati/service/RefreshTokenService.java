package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.config.JwtService;
import com.wakati.entity.RefreshToken;
import com.wakati.entity.User;
import com.wakati.exception.WakatiException;
import com.wakati.repository.RefreshTokenRepository;
import com.wakati.repository.UserRepository;
import com.wakati.util.RefreshTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Transactional
    public Map<String, Object> refresh(String providedToken,
                                       HttpServletRequest request) {

        if (providedToken == null || providedToken.isBlank()) {
            throw new WakatiException(I18NConstants.REFRESH_TOKEN_REQUIRED);
        }

        String hash = RefreshTokenUtil.hash(providedToken);

        // 🔒 LOCK row (equivalent to FOR UPDATE)
        RefreshToken token = repository.findForUpdate(hash)
                .orElseThrow(() -> new WakatiException(I18NConstants.INVALID_REFRESH_TOKEN));

        // ❌ Already used (token reuse attack)
        if (token.getRevoked()) {

            repository.revokeAllByUserId(token.getUser().getUserId());

            throw new WakatiException(I18NConstants.REFRESH_TOKEN_REUSED);
        }

        // ❌ Expired
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new WakatiException(I18NConstants.REFRESH_TOKEN_EXPIRED);
        }

        // ✅ Generate new token
        String newPlain = RefreshTokenUtil.generatePlain();
        String newHash = RefreshTokenUtil.hash(newPlain);

        // ✅ Rotate (atomic)
        token.setRevoked(true);
        token.setReplacedBy(newHash);
        token.setLastUsedAt(LocalDateTime.now());

        RefreshToken newToken = new RefreshToken();
        newToken.setUser(token.getUser());
        newToken.setTokenHash(newHash);
        newToken.setExpiresAt(RefreshTokenUtil.expiresInDays(30));
        newToken.setIp(request.getRemoteAddr());
        newToken.setUserAgent(request.getHeader("User-Agent"));

        repository.save(token);
        repository.save(newToken);

        // ✅ Generate new JWT
        User user = userRepository.findByUserId(token.getUser().getUserId())
                .orElseThrow();

        Map<String, Object> payload = Map.of(
                "id", user.getUserId(),
                "sid", user.getSessionToken()
        );

        String jwt = jwtService.generateToken(payload, 3600);

        return Map.of(
                "code", 200,
                "message", "Token refreshed",
                "access_token", jwt,
                "expires_in", 3600,
                "refresh_token", newPlain,
                "refresh_expires_in", 30 * 24 * 3600
        );
    }
}