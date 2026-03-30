package com.wakati.entity;

import com.wakati.enums.CommissionMode;
import com.wakati.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "BULK_TRANSFER_BATCH")
public class BulkTransferBatch extends BaseCreatedAtEntity{

    @Id
    private String batchId;

    @ManyToOne
    @JoinColumn(name = "source_user_id", referencedColumnName = "user_id")
    private User sourceUser;

    @ManyToOne
    @JoinColumn(name = "initiated_by", referencedColumnName = "user_id")
    private User initiatedBy;

    @ManyToOne
    @JoinColumn(name = "requested_by", referencedColumnName = "user_id")
    private User requestedBy;

    private Integer transactionCount;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private CommissionMode commissionMode;

    private Double commissionPercentage;
    private Double totalCommission;
    private Double totalDebit;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    private String rowsJson;

    @ManyToOne
    @JoinColumn(name = "reviewed_by", referencedColumnName = "user_id")
    private User reviewedBy;

    private LocalDateTime reviewedAt;

    private String rejectionReason;


    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public User getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(User sourceUser) {
        this.sourceUser = sourceUser;
    }

    public User getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(User initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CommissionMode getCommissionMode() {
        return commissionMode;
    }

    public void setCommissionMode(CommissionMode commissionMode) {
        this.commissionMode = commissionMode;
    }

    public Double getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(Double commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public Double getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(Double totalCommission) {
        this.totalCommission = totalCommission;
    }

    public Double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRowsJson() {
        return rowsJson;
    }

    public void setRowsJson(String rowsJson) {
        this.rowsJson = rowsJson;
    }

    public User getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(User reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}