package com.wakati.model.response;

import java.math.BigDecimal;

public class TransactionResponse {
    private String code;
    private String message;
    private String traceId;
    private String txnId;
    private String txnType;
    private BigDecimal amount;
    private BigDecimal commission;
    private String smsResponse;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getSmsResponse() {
        return smsResponse;
    }

    public void setSmsResponse(String smsResponse) {
        this.smsResponse = smsResponse;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", traceId='" + traceId + '\'' +
                ", txnId='" + txnId + '\'' +
                ", txnType='" + txnType + '\'' +
                ", amount=" + amount +
                ", commission=" + commission +
                ", smsResponse='" + smsResponse + '\'' +
                '}';
    }
}
