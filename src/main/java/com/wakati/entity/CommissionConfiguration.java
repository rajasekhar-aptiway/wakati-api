package com.wakati.entity;

import com.wakati.enums.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "COMMISSION_CONFIGURATION",
        uniqueConstraints = @UniqueConstraint(columnNames = {"channel","txn_type","status"}))
public class CommissionConfiguration extends BaseUpdatedAtEntity{

    @Id
    private String commissionId;

    @Enumerated(EnumType.STRING)
    private TxnType txnType;

    @Enumerated(EnumType.STRING)
    private CommissionChannel channel;

    private BigDecimal percent;
    private BigDecimal platformPercent;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String createdBy;
    private String updatedBy;


    public String getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(String commissionId) {
        this.commissionId = commissionId;
    }

    public TxnType getTxnType() {
        return txnType;
    }

    public void setTxnType(TxnType txnType) {
        this.txnType = txnType;
    }

    public CommissionChannel getChannel() {
        return channel;
    }

    public void setChannel(CommissionChannel channel) {
        this.channel = channel;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public BigDecimal getPlatformPercent() {
        return platformPercent;
    }

    public void setPlatformPercent(BigDecimal platformPercent) {
        this.platformPercent = platformPercent;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}