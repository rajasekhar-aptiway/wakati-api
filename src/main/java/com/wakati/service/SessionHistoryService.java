package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.config.UserContextHolder;
import com.wakati.entity.User;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.SessionHistoryRequest;
import com.wakati.repository.SessionHistoryRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SessionHistoryService {

    @Autowired
    private SessionHistoryRepository repo;

    public Map<String, Object> getSessionHistory(SessionHistoryRequest req) {

        String userId = Optional.ofNullable(req.getUserId()).orElse("").trim();

        if (userId.isEmpty()) {
            throw new RuntimeException("user_id is required");
        }

        int page = Math.max(1, Optional.ofNullable(req.getPage()).orElse(1));
        int pageSize = Math.max(1, Math.min(50, Optional.ofNullable(req.getPageSize()).orElse(20)));
        int offset = (page - 1) * pageSize;

        // ✅ AUTH CONTEXT (equivalent of require_jwt.php)
        User user = UserContextHolder.getUser();
        String authUserType = user.getUserType().name();
        String authUserId = user.getUserId();

        List<String> adminRoles = List.of("ADMIN", "CFO", "ADJUDICATOR");

        if (!userId.equals(authUserId) && !adminRoles.contains(authUserType)) {
            throw new WakatiException(I18NConstants.USER_NOT_FOUND);
        }

        // ✅ COUNT
        long total = repo.countSessions(userId);

        // ✅ FETCH DATA
        List<Map<String, Object>> sessions =
                repo.fetchSessions(userId, pageSize, offset);

        // ✅ FORMAT (same as PHP)
        List<Map<String, Object>> rows = sessions.stream().map(s -> Map.of(
                "session_token", s.get("session_token"),
                "ip_address", s.get("ip_address"),
                "user_agent", s.get("user_agent"),
                "login_at", s.get("login_at"),
                "logout_at", s.get("logout_at"),
                "logout_type", s.get("logout_type"),
                "duration_seconds",
                    s.get("duration_seconds") != null
                            ? ((Number) s.get("duration_seconds")).intValue()
                            : null,
                "active", s.get("logout_at") == null
        )).toList();

        return Map.of(
                "code", 200,
                "trace_id", MDC.get("traceId"), // from your trace filter
                "user_id", userId,
                "sessions", rows,
                "pagination", Map.of(
                        "page", page,
                        "page_size", pageSize,
                        "total", total,
                        "total_pages", total > 0 ? (int) Math.ceil((double) total / pageSize) : 0
                )
        );
    }
}