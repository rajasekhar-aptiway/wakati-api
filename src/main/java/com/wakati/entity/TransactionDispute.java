package com.wakati.entity;

import jakarta.persistence.*;
import com.wakati.enums.DisputeStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "TRANSACTION_DISPUTES")
public class TransactionDispute {

    @Id
    private String disputeId;

    @ManyToOne
    @JoinColumn(name = "txn_id", referencedColumnName = "txn_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "raised_by", referencedColumnName = "user_id")
    private User raisedBy;

    private String reason;

    @Enumerated(EnumType.STRING)
    private DisputeStatus status;

    @ManyToOne
    @JoinColumn(name = "resolved_by", referencedColumnName = "user_id")
    private User resolvedBy;

    private String resolution;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(String disputeId) {
        this.disputeId = disputeId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public User getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(User raisedBy) {
        this.raisedBy = raisedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DisputeStatus getStatus() {
        return status;
    }

    public void setStatus(DisputeStatus status) {
        this.status = status;
    }

    public User getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(User resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}