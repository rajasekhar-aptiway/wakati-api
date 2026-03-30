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

    private String securityDeposit;
    private String depositMinLimit;
    private String depositMaxLimit;
    private String withdrawalMinLimit;
    private String withdrawalMaxLimit;
    private String minimumOpeningBalance;



    private Double dailyDepositLimit;
    private Double dailyWithdrawalLimit;

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

    public String getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(String securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getDepositMinLimit() {
        return depositMinLimit;
    }

    public void setDepositMinLimit(String depositMinLimit) {
        this.depositMinLimit = depositMinLimit;
    }

    public String getDepositMaxLimit() {
        return depositMaxLimit;
    }

    public void setDepositMaxLimit(String depositMaxLimit) {
        this.depositMaxLimit = depositMaxLimit;
    }

    public String getWithdrawalMinLimit() {
        return withdrawalMinLimit;
    }

    public void setWithdrawalMinLimit(String withdrawalMinLimit) {
        this.withdrawalMinLimit = withdrawalMinLimit;
    }

    public String getWithdrawalMaxLimit() {
        return withdrawalMaxLimit;
    }

    public void setWithdrawalMaxLimit(String withdrawalMaxLimit) {
        this.withdrawalMaxLimit = withdrawalMaxLimit;
    }

    public String getMinimumOpeningBalance() {
        return minimumOpeningBalance;
    }

    public void setMinimumOpeningBalance(String minimumOpeningBalance) {
        this.minimumOpeningBalance = minimumOpeningBalance;
    }


    public Double getDailyDepositLimit() {
        return dailyDepositLimit;
    }

    public void setDailyDepositLimit(Double dailyDepositLimit) {
        this.dailyDepositLimit = dailyDepositLimit;
    }

    public Double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(Double dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }
}