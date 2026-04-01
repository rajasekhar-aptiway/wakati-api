package com.wakati.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private String transactionId;
    private String category;
    private String type;
    private String channel;
    private String initiatedBy;

    private String direction;

    private String senderUserId;
    private String senderName;
    private String senderMobile;

    private String receiverUserId;
    private String receiverName;
    private String receiverMobile;

    private BigDecimal transactionAmount;
    private String ledgerEntryType;
    private BigDecimal remainingBalance;

    private LocalDateTime createdAt;
    private String remarks;

    // Private constructor
    private TransactionResponse() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final TransactionResponse obj = new TransactionResponse();

        public Builder transactionId(String val) { obj.transactionId = val; return this; }
        public Builder category(String val) { obj.category = val; return this; }
        public Builder type(String val) { obj.type = val; return this; }
        public Builder channel(String val) { obj.channel = val; return this; }
        public Builder initiatedBy(String val) { obj.initiatedBy = val; return this; }

        public Builder direction(String val) { obj.direction = val; return this; }

        public Builder senderUserId(String val) { obj.senderUserId = val; return this; }
        public Builder senderName(String val) { obj.senderName = val; return this; }
        public Builder senderMobile(String val) { obj.senderMobile = val; return this; }

        public Builder receiverUserId(String val) { obj.receiverUserId = val; return this; }
        public Builder receiverName(String val) { obj.receiverName = val; return this; }
        public Builder receiverMobile(String val) { obj.receiverMobile = val; return this; }

        public Builder transactionAmount(BigDecimal val) { obj.transactionAmount = val; return this; }
        public Builder ledgerEntryType(String val) { obj.ledgerEntryType = val; return this; }
        public Builder remainingBalance(BigDecimal val) { obj.remainingBalance = val; return this; }

        public Builder createdAt(LocalDateTime val) { obj.createdAt = val; return this; }
        public Builder remarks(String val) { obj.remarks = val; return this; }

        public TransactionResponse build() {
            return obj;
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getLedgerEntryType() {
        return ledgerEntryType;
    }

    public void setLedgerEntryType(String ledgerEntryType) {
        this.ledgerEntryType = ledgerEntryType;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
