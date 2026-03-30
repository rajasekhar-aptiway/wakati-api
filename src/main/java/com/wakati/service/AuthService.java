package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.entity.*;
import com.wakati.enums.SecurityEventType;
import com.wakati.enums.Status;
import com.wakati.enums.UserType;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.LoginRequest;
import com.wakati.repository.*;
import com.wakati.util.RefreshTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.wakati.I18NConstants.*;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCredentialsRepository credentialsRepository;
    @Autowired
    private RefreshTokenRepository refreshRepo;
    @Autowired
    private SessionLogRepository sessionLogRepo;
    @Autowired
    private SecurityEventLogRepository securityLogRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MessageService messageService;

    @Transactional
    public Map<String, Object> login(LoginRequest request, HttpServletRequest http) {

        String mobile = request.getMobileNumber();
        String password = request.getPassword();
        String fcmToken = request.getFcmToken();

        String ip = http.getRemoteAddr();
        String userAgent = http.getHeader("User-Agent");

        if (mobile == null || password == null) {
            throw new WakatiException(INVALID_LOGIN_INPUT);
        }

        // ✅ Fetch user
        User user = userRepository.findUsersByMobileOrdered(mobile)
                .stream()
                .findFirst()
                .orElseThrow(() -> new WakatiException(MOBILE_NOT_REGISTERED));

        UserCredentials cred = credentialsRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new WakatiException(I18NConstants.INVALID_CREDENTIALS));

        // ✅ Password check (bcrypt + legacy)
        String stored = cred.getPassword();

        if (stored.startsWith("$2a") || stored.startsWith("$2b")) {
            if (!passwordEncoder.matches(password, stored)) {
                logFail(mobile, ip, userAgent);
                throw new WakatiException(I18NConstants.INVALID_CREDENTIALS);
            }
        } else {
            if (!password.equals(stored)) {
                logFail(mobile, ip, userAgent);
                throw new WakatiException(I18NConstants.INVALID_CREDENTIALS);
            }

            // upgrade
            cred.setPassword(passwordEncoder.encode(password));
            credentialsRepository.save(cred);
        }

        // ✅ Status check
        if (user.getStatus() != Status.APPROVED) {
            throw new WakatiException(I18NConstants.ACCOUNT_NOT_ACTIVE);
        }

        // ✅ Dealer check
        if (user.getUserType() == UserType.DEALER &&
            !userRepository.hasSecurityDeposit(user.getUserId())) {

            throw new WakatiException(DEALER_NOT_ACTIVATED);
        }

        // ✅ PIN check
        boolean pinSetup = cred.getPinHash() == null;

        boolean firstLogin = user.getLastLogin() == null;

        // ✅ Session token (invalidate old sessions)
        String sessionToken = UUID.randomUUID().toString();

        // ✅ JWT
        Map<String, Object> payload = Map.of(
                "id", user.getUserId(),
                "sid", sessionToken
        );

        String jwt = jwtService.generateToken(payload, 3600);

        // ✅ Refresh token
        String refreshPlain = RefreshTokenUtil.generatePlain();
        String refreshHash = RefreshTokenUtil.hash(refreshPlain);

        // 🔥 Transaction operations

        // 1. Close old sessions
        sessionLogRepo.closeActiveSessions(user.getUserId());

        // 2. Delete old refresh tokens
        refreshRepo.deleteByUserId(user.getUserId());

        // 3. Insert new refresh token
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setTokenHash(refreshHash);
        token.setExpiresAt(RefreshTokenUtil.expiresInDays(30));
        token.setIp(ip);
        token.setUserAgent(userAgent);

        refreshRepo.save(token);

        // 4. Update user
        user.setSessionToken(sessionToken);
        user.setFcmToken(fcmToken);
        user.setLastLogin(LocalDateTime.now().toString());
        userRepository.save(user);

        // 5. Insert session log
        SessionLog sessionLog = new SessionLog();
        sessionLog.setSessionToken(sessionToken);
        sessionLog.setUser(user);
        sessionLog.setIpAddress(ip);
        sessionLog.setUserAgent(userAgent);
        sessionLog.setLoginAt(LocalDateTime.now());
        sessionLogRepo.save(sessionLog);

        // 6. Security log
        SecurityEventLog eventLog = new SecurityEventLog();
        eventLog.setEventType(SecurityEventType.LOGIN_SUCCESS);
        eventLog.setUser(user);
        eventLog.setIdentifier(mobile);
        eventLog.setIpAddress(ip);
        eventLog.setUserAgent(userAgent);
        securityLogRepo.save(eventLog);

        return Map.of(
                "code", 200,
                "message", messageService.get(I18NConstants.LOGIN_SUCCESS),
                "token", jwt,
                "expires_in", 3600,
                "refresh_token", refreshPlain,
                "refresh_expires_in", 30 * 24 * 3600,
                "first_time_login", firstLogin,
                "pin_setup", pinSetup,
                "response", Map.of(
                        "user_id", user.getUserId(),
                        "user_type", user.getUserType(),
                        "fullName", user.getFullName()
                )
        );
    }

    private void logFail(String mobile, String ip, String ua) {
        SecurityEventLog eventLog = new SecurityEventLog();
        eventLog.setEventType(SecurityEventType.LOGIN_FAIL);
        eventLog.setIdentifier(mobile);
        eventLog.setIpAddress(ip);
        eventLog.setUserAgent(ua);

        securityLogRepo.save(eventLog);
    }
}