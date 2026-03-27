package com.wakati.model.response.DTO;

import com.wakati.enums.VerificationStatus;

public class DocumentDTO {
    private String documentId;
    private String documentType;
    private String documentNumber;
    private String documentUrl;
    private VerificationStatus verificationStatus;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public DocumentDTO(String documentId, String documentType, String documentNumber, String documentUrl, VerificationStatus verificationStatus) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.documentUrl = documentUrl;
        this.verificationStatus = verificationStatus;
    }
}