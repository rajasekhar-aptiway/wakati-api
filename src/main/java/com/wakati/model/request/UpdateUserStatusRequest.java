package com.wakati.model.request;

import com.wakati.enums.Status;

public class UpdateUserStatusRequest {
    private String userId;
    private Status status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}