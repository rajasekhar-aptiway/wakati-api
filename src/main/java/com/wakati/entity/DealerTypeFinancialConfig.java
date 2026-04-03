package com.wakati.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DEALER_TYPE_FINANCIAL_CONFIG")
public class DealerTypeFinancialConfig extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String dealerTypeId;

    private BigDecimal securityDeposit;
    private BigDecimal depositMinLimit;
    private BigDecimal depositMaxLimit;
    private BigDecimal withdrawalMinLimit;
    private BigDecimal withdrawalMaxLimit;
    private BigDecimal minimumOpeningBalance;



    private BigDecimal dailyDepositLimit;
    private BigDecimal dailyWithdrawalLimit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDealerTypeId() {
        return dealerTypeId;
    }

    public void setDealerTypeId(String dealerTypeId) {
        this.dealerTypeId = dealerTypeId;
    }

    public BigDecimal getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public BigDecimal getDepositMinLimit() {
        return depositMinLimit;
    }

    public void setDepositMinLimit(BigDecimal depositMinLimit) {
        this.depositMinLimit = depositMinLimit;
    }

    public BigDecimal getDepositMaxLimit() {
        return depositMaxLimit;
    }

    public void setDepositMaxLimit(BigDecimal depositMaxLimit) {
        this.depositMaxLimit = depositMaxLimit;
    }

    public BigDecimal getWithdrawalMinLimit() {
        return withdrawalMinLimit;
    }

    public void setWithdrawalMinLimit(BigDecimal withdrawalMinLimit) {
        this.withdrawalMinLimit = withdrawalMinLimit;
    }

    public BigDecimal getWithdrawalMaxLimit() {
        return withdrawalMaxLimit;
    }

    public void setWithdrawalMaxLimit(BigDecimal withdrawalMaxLimit) {
        this.withdrawalMaxLimit = withdrawalMaxLimit;
    }

    public BigDecimal getMinimumOpeningBalance() {
        return minimumOpeningBalance;
    }

    public void setMinimumOpeningBalance(BigDecimal minimumOpeningBalance) {
        this.minimumOpeningBalance = minimumOpeningBalance;
    }

    public BigDecimal getDailyDepositLimit() {
        return dailyDepositLimit;
    }

    public void setDailyDepositLimit(BigDecimal dailyDepositLimit) {
        this.dailyDepositLimit = dailyDepositLimit;
    }

    public BigDecimal getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(BigDecimal dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }
}