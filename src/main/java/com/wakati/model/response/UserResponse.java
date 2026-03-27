package com.wakati.model.response;


import com.wakati.enums.Status;
import com.wakati.enums.VerificationStatus;
import com.wakati.model.response.DTO.AttributeDTO;
import com.wakati.model.response.DTO.DocumentDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserResponse {

    private  String userId;
    private String walletId;
    private String userType;
    private String fullName;
    private String mobileNo;
    private String email;
    private Status status;
    private String createdBy;
    private LocalDateTime createdAt;

    private String registrationStage;
    private VerificationStatus verifiedByAdmin;
    private VerificationStatus verifiedByAdjudicator;

    private List<AttributeDTO> attributes;
    private List<DocumentDTO> documents;

    // ✅ Default Constructor
    public UserResponse() {
        this.attributes = new ArrayList<>();
        this.documents = new ArrayList<>();
    }

    // ✅ All Args Constructor
    public UserResponse(String userId, String walletId, String userType, String fullName,
                        String mobileNo, String email, Status status, String createdBy,
                        LocalDateTime createdAt, String registrationStage,
                        VerificationStatus verifiedByAdmin, VerificationStatus verifiedByAdjudicator,
                        List<AttributeDTO> attributes, List<DocumentDTO> documents) {
        this.userId = userId;
        this.walletId = walletId;
        this.userType = userType;
        this.fullName = fullName;
        this.mobileNo = mobileNo;
        this.email = email;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.registrationStage = registrationStage;
        this.verifiedByAdmin = verifiedByAdmin;
        this.verifiedByAdjudicator = verifiedByAdjudicator;
        this.attributes = attributes != null ? attributes : new ArrayList<>();
        this.documents = documents != null ? documents : new ArrayList<>();
    }

    // ✅ Getters and Setters

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getWalletId() { return walletId; }
    public void setWalletId(String walletId) { this.walletId = walletId; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getRegistrationStage() { return registrationStage; }
    public void setRegistrationStage(String registrationStage) { this.registrationStage = registrationStage; }

    public VerificationStatus getVerifiedByAdmin() { return verifiedByAdmin; }
    public void setVerifiedByAdmin(VerificationStatus verifiedByAdmin) { this.verifiedByAdmin = verifiedByAdmin; }

    public VerificationStatus getVerifiedByAdjudicator() { return verifiedByAdjudicator; }
    public void setVerifiedByAdjudicator(VerificationStatus verifiedByAdjudicator) { this.verifiedByAdjudicator = verifiedByAdjudicator; }

    public List<AttributeDTO> getAttributes() { return attributes; }
    public void setAttributes(List<AttributeDTO> attributes) { this.attributes = attributes; }

    public List<DocumentDTO> getDocuments() { return documents; }
    public void setDocuments(List<DocumentDTO> documents) { this.documents = documents; }

    // ✅ Add Attribute (No duplicates)
    public void addAttribute(UserProjection row) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }

        boolean exists = attributes.stream()
                .anyMatch(a -> a.getAttributeKey().equals(row.getAttributeKey()));

        if (!exists) {
            attributes.add(new AttributeDTO(
                    row.getAttributeId(),
                    row.getAttributeKey(),
                    row.getAttributeValue()
            ));
        }
    }

    // ✅ Add Document (No duplicates)
    public void addDocument(UserProjection row) {
        if (documents == null) {
            documents = new ArrayList<>();
        }

        boolean exists = documents.stream()
                .anyMatch(d -> d.getDocumentId().equals(row.getDocumentId()));

        if (!exists) {
            documents.add(new DocumentDTO(
                    row.getDocumentId(),
                    row.getDocumentType(),
                    row.getDocumentNumber(),
                    row.getDocumentUrl(),
                    row.getVerificationStatus()
            ));
        }
    }

    // ✅ Builder Pattern (Manual)
    public static class Builder {

        private String userId;
        private String walletId;
        private String userType;
        private String fullName;
        private String mobileNo;
        private String email;
        private Status status;
        private String createdBy;
        private LocalDateTime createdAt;
        private String registrationStage;
        private VerificationStatus verifiedByAdmin;
        private VerificationStatus verifiedByAdjudicator;
        private List<AttributeDTO> attributes = new ArrayList<>();
        private List<DocumentDTO> documents = new ArrayList<>();

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder walletId(String walletId) { this.walletId = walletId; return this; }
        public Builder userType(String userType) { this.userType = userType; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder mobileNo(String mobileNo) { this.mobileNo = mobileNo; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder registrationStage(String registrationStage) { this.registrationStage = registrationStage; return this; }
        public Builder verifiedByAdmin(VerificationStatus verifiedByAdmin) { this.verifiedByAdmin = verifiedByAdmin; return this; }
        public Builder verifiedByAdjudicator(VerificationStatus verifiedByAdjudicator) { this.verifiedByAdjudicator = verifiedByAdjudicator; return this; }
        public Builder attributes(List<AttributeDTO> attributes) { this.attributes = attributes; return this; }
        public Builder documents(List<DocumentDTO> documents) { this.documents = documents; return this; }

        public UserResponse build() {
            return new UserResponse(
                    userId, walletId, userType, fullName,
                    mobileNo, email, status, createdBy,
                    createdAt, registrationStage,
                    verifiedByAdmin, verifiedByAdjudicator,
                    attributes, documents
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}