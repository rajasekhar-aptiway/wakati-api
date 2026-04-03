package com.wakati.model.request;

public class LogEntry {
    private String timestamp;
    private String level;
    private String message;
    private String traceId;

    public LogEntry(String timestamp, String level, String message, String traceId) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.traceId = traceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getTraceId() {
        return traceId;
    }
}