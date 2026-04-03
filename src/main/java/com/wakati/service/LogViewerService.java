package com.wakati.service;

import com.wakati.config.UserContextHolder;
import com.wakati.entity.User;
import com.wakati.exception.WakatiException;
import com.wakati.model.request.LogEntry;
import com.wakati.model.request.LogViewerRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wakati.I18NConstants.*;

@Service
public class LogViewerService {

    private AppLoggerService logger;

    public Map<String, Object> handle(LogViewerRequest req) {

        User user = UserContextHolder.getUser();

        // ✅ AUTH
        if (!List.of("ADMIN", "CFO").contains(user.getUserType().name())) {
            throw new WakatiException(HttpStatus.FORBIDDEN,CFO_AND_ADMIN_REQUIRED);
        }

        String action = Optional.ofNullable(req.getAction()).orElse("read").toLowerCase();

        return switch (action) {
            case "dates" -> getDates();
            case "read" -> readLogs(req);
            default -> throw new WakatiException(INVALID_LOG_ACTION);
        };
    }

    private Map<String, Object> getDates() {

        List<String> dates = logger.availableDates();

        return Map.of(
                "code", 200,
                "trace_id", MDC.get("traceId"),
                "dates", dates,
                "count", dates.size()
        );
    }

    private Map<String, Object> readLogs(LogViewerRequest req) {

        String date = Optional.ofNullable(req.getDate())
                .orElse(LocalDate.now().toString());

        String level = Optional.ofNullable(req.getLevel()).orElse("").toUpperCase();
        String keyword = Optional.ofNullable(req.getKeyword()).orElse("");

        int page = Math.max(1, Optional.ofNullable(req.getPage()).orElse(1));
        int size = Math.max(1, Math.min(500, Optional.ofNullable(req.getPageSize()).orElse(100)));

        // ✅ VALIDATE DATE
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new WakatiException(INVALID_DATE_FORMAT);
        }

        List<String> validLevels = List.of("DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL", "");
        if (!validLevels.contains(level)) {
            throw new WakatiException(INVALID_LOG_LEVEL);
        }

        // ✅ READ FILE
        List<LogEntry> entries = logger.readDate(date, level, keyword);

        // newest first
        Collections.reverse(entries);

        int total = entries.size();
        int offset = (page - 1) * size;

        List<LogEntry> paged =
                entries.stream()
                        .skip(offset)
                        .limit(size)
                        .toList();

        // ✅ SUMMARY
        Map<String, Long> summary = entries.stream()
                .collect(Collectors.groupingBy(
                        LogEntry::getLevel,
                        Collectors.counting()
                ));

        // fill missing levels
        for (String lvl : List.of("DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL")) {
            summary.putIfAbsent(lvl, 0L);
        }

        return Map.of(
                "code", 200,
                "trace_id", MDC.get("traceId"),
                "date", date,
                "filters", Map.of(
                        "level", level.isEmpty() ? null : level,
                        "keyword", keyword.isEmpty() ? null : keyword
                ),
                "summary", summary,
                "entries", paged,
                "pagination", Map.of(
                        "page", page,
                        "page_size", size,
                        "total", total,
                        "total_pages", total > 0 ? (int) Math.ceil((double) total / size) : 0
                )
        );
    }
}