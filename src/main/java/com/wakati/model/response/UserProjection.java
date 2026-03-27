package com.wakati.model.response;

import com.wakati.enums.Status;
import com.wakati.enums.VerificationStatus;

import java.time.LocalDateTime;

public interface UserProjection {

    String getUserId();
    String getUserType();
    String getFullName();
    String getMobileNo();
    String getEmail();
    Status getStatus();
    String getCreatedBy();
    LocalDateTime getCreatedAt();

    String getRegistrationStage();
    VerificationStatus getVerifiedByAdmin();
    VerificationStatus getVerifiedByAdjudicator();

    String getAttributeId();
    String getAttributeKey();
    String getAttributeValue();

    String getDocumentId();
    String getDocumentType();
    String getDocumentNumber();
    String getDocumentUrl();
    VerificationStatus getVerificationStatus();

    String getWalletId();
}