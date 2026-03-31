package com.wakati.config;

import com.wakati.enums.Status;

public class UserStatusEvent {
    private final String userId;
    private final Status status;

    public UserStatusEvent(String userId, Status status) {
        this.userId = userId;
        this.status = status;
    }

    public String getUserId() { return userId; }
    public Status getStatus() { return status; }
}