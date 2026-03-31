package com.wakati.config;

import com.wakati.I18NConstants;
import com.wakati.entity.User;
import com.wakati.enums.Status;
import com.wakati.repository.UserRepository;
import com.wakati.service.JwtService;
import com.wakati.service.MessageService;
import com.wakati.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = JwtUtils.getBearerToken(request);

            // ✅ 1. Token missing
            if (token == null) {
                sendError(response, 401, messageService.get(I18NConstants.AUTHORIZATION_REQUIRED));
                return;
            }

            // ✅ 2. Validate token
            JwtValidationResult result = jwtService.validateToken(token);

            if (!result.isValid()) {
                sendError(response, 401, result.getMessage());
                return;
            }

            Map<String, Object> payload = result.getPayload();

            String userId = (String) payload.get("id");
            String tokenSid = (String) payload.get("sid");

            if (userId == null) {
                sendError(response, 401, messageService.get(I18NConstants.INVALID_TOKEN));
                return;
            }

            // ✅ 3. Fetch user
            User user = userRepository.findByUserId(userId)
                    .orElse(null);

            if (user == null) {
                sendError(response, 401, messageService.get(I18NConstants.USER_NOT_FOUND));
                return;
            }

            // ✅ 4. Single session check
            String activeSid = user.getSessionToken();

            if (tokenSid == null || !tokenSid.equals(activeSid)) {
                sendError(response, 401, messageService.get(I18NConstants.SESSION_INVALIDATED_PLEASE_LOGIN_AGAIN));
                return;
            }

            // ✅ 5. Status check
            if (user.getStatus() != Status.APPROVED) {
                sendError(response, 403, messageService.get(I18NConstants.ACCOUNT_IS_NOT_ACTIVE));
                return;
            }

            // ✅ 6. Store user context (VERY IMPORTANT)
            UserContextHolder.setUser(user);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear(); // 🔥 critical
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/")
                || path.equals("/api/register");
    }

    private void sendError(HttpServletResponse response, int code, String message) throws IOException {

        response.setStatus(code);
        response.setContentType("application/json");

        response.getWriter().write("""
                    {
                        "code": %d,
                        "message": "%s"
                    }
                """.formatted(code, message));
    }
}