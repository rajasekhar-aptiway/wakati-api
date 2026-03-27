package com.wakati.model.response;

import java.time.LocalDateTime;

public interface UserWithExpiryProjection {

    String getUserId();
    String getUserType();
    String getFullName();
    String getMobileNo();
    String getEmail();
    String getStatus();
    String getCreatedBy();
    LocalDateTime getCreatedAt();
    String getIdExpiry();
}