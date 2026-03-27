package com.wakati.entity;

import jakarta.persistence.*;
import com.wakati.enums.RequestedByType;
import com.wakati.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMER_CLOSURE_REQUEST")
public class CustomerClosureRequest {

    @Id
    private String closureRequestId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "requested_by", referencedColumnName = "user_id")
    private User requestedBy;

    private String requestedByType;
    private String status;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    private User approvedBy;

    private LocalDateTime approvedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getClosureRequestId() {
        return closureRequestId;
    }

    public void setClosureRequestId(String closureRequestId) {
        this.closureRequestId = closureRequestId;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getRequestedByType() {
        return requestedByType;
    }

    public void setRequestedByType(String requestedByType) {
        this.requestedByType = requestedByType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
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