package com.wakati.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DEALER_SECURITY_DEPOSITS")
public class DealerSecurityDeposits {


    @OneToOne
    @JoinColumn(name = "dealer_id", referencedColumnName = "user_id")
    private User dealer;

    @Id
    private String code;

    private Double securityDepositAmount;
    private Double openingBalance;

    private String createdBy;
    private String createdByUserId;

    private String disconnecton;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public User getDealer() {
        return dealer;
    }

    public void setDealer(User dealer) {
        this.dealer = dealer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getSecurityDepositAmount() {
        return securityDepositAmount;
    }

    public void setSecurityDepositAmount(Double securityDepositAmount) {
        this.securityDepositAmount = securityDepositAmount;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
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

    public String getDisconnecton() {
        return disconnecton;
    }

    public void setDisconnecton(String disconnecton) {
        this.disconnecton = disconnecton;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
}