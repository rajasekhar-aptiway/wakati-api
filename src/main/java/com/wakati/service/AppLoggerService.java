package com.wakati.service;

import com.wakati.model.request.LogEntry;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class AppLoggerService {

    private static final String LOG_DIR = "logs"; // same as PHP

    public List<String> availableDates() {

        File dir = new File(LOG_DIR);

        if (!dir.exists()) return List.of();

        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .map(File::getName)
                .filter(name -> name.endsWith(".log"))
                .map(name -> name.replace(".log", ""))
                .sorted()
                .toList();
    }

    public List<LogEntry> readDate(String date, String level, String keyword) {

        File file = new File(LOG_DIR + "/" + date + ".log");

        if (!file.exists()) return List.of();

        try {
            return Files.lines(file.toPath())
                    .map(this::parseLine)
                    .filter(Objects::nonNull)
                    .filter(e -> level.isEmpty() || level.equals(e.getLevel()))
                    .filter(e -> keyword.isEmpty() || e.getMessage().contains(keyword))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read logs");
        }
    }

    private LogEntry parseLine(String line) {

        // Example expected format:
        // [2026-01-01 10:00:00] [INFO] [traceId=xxx] message

        try {
            String timestamp = line.substring(1, 20);
            String level = extract(line, "[", "]", 2);
            String traceId = extract(line, "traceId=", "]");
            String message = line.substring(line.lastIndexOf("]") + 1).trim();

            return new LogEntry(timestamp, level, message, traceId);

        } catch (Exception e) {
            return null;
        }
    }

    private String extract(String text, String start, String end, int occurrence) {
        int from = 0;
        for (int i = 0; i < occurrence; i++) {
            from = text.indexOf(start, from) + 1;
        }
        int to = text.indexOf(end, from);
        return text.substring(from, to);
    }

    private String extract(String text, String key, String end) {
        int start = text.indexOf(key);
        if (start == -1) return null;
        start += key.length();
        int stop = text.indexOf(end, start);
        return text.substring(start, stop);
    }
}