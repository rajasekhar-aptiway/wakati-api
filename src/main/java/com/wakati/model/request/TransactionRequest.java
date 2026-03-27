package com.wakati.model.request;

import com.wakati.enums.TransactionType;

import java.math.BigDecimal;

public class TransactionRequest {
    private String sourceUserId;
    private String targetUserId;
    private BigDecimal amount;
    private TransactionType txnType;
    private String remarks;
    private String initiatedBy;

    public String getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTxnType() {
        return txnType;
    }

    public void setTxnType(TransactionType txnType) {
        this.txnType = txnType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "sourceUserId='" + sourceUserId + '\'' +
                ", targetUserId='" + targetUserId + '\'' +
                ", amount=" + amount +
                ", txnType='" + txnType + '\'' +
                ", remarks='" + remarks + '\'' +
                ", initiatedBy='" + initiatedBy + '\'' +
                '}';
    }
}
