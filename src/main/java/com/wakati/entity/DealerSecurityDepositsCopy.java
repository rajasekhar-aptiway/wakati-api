package com.wakati.entity;

import jakarta.persistence.*;
import com.wakati.enums.disconnecton;

import java.time.LocalDateTime;

@Entity
@Table(name = "DEALER_SECURITY_SEPOSITS_COPY")
public class DealerSecurityDepositsCopy {
    @Id
    private Long id;
    @Column(name = "code")
    private String code ;
    @Column(name = "dealer_id")
    private String dealerId ;
    @Column(name = "security_deposit_amount")
    private String securityDepositAmount;
    @Column(name = "opening_balance")
    private String openingBalance ;
    @Column(name = "disconnection")
    @Enumerated(EnumType.STRING)
    private disconnecton disconnecton ;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_by_user_id")
    private String createdByUserId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getSecurityDepositAmount() {
        return securityDepositAmount;
    }

    public void setSecurityDepositAmount(String securityDepositAmount) {
        this.securityDepositAmount = securityDepositAmount;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public disconnecton getDisconnecton() {
        return disconnecton;
    }

    public void setDisconnecton(disconnecton disconnecton) {
        this.disconnecton = disconnecton;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
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

    @Override
    public String toString() {
        return "DealerSecurityDepositsCopy{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", dealerId='" + dealerId + '\'' +
                ", securityDepositAmount='" + securityDepositAmount + '\'' +
                ", openingBalance='" + openingBalance + '\'' +
                ", disconnecton=" + disconnecton +
                ", createdBy='" + createdBy + '\'' +
                ", createdByUserId='" + createdByUserId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
