package com.wakati.model.request.DTO;


import java.math.BigDecimal;

public class TransactionLimitRequest {

    private String action; // get / set

    private String customerId;

    private BigDecimal dailyTransferLimit;

    private BigDecimal singleTransferLimit;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(BigDecimal dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public BigDecimal getSingleTransferLimit() {
        return singleTransferLimit;
    }

    public void setSingleTransferLimit(BigDecimal singleTransferLimit) {
        this.singleTransferLimit = singleTransferLimit;
    }
}