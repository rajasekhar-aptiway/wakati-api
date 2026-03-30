package com.wakati.entity;

import com.wakati.enums.Status;
import com.wakati.enums.TreasuryTxnType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TREASURY_ACTIONS")
public class TreasuryActions extends BaseUpdatedAtEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String treasuryTxnId;
    private String systemAccountId;

    @Enumerated(EnumType.STRING)
    private TreasuryTxnType txnType;

    private String amount;
    private String referenceNo;

    @ManyToOne
    @JoinColumn(name = "performed_by", referencedColumnName = "user_id")
    private User performedBy;

    @ManyToOne
    @JoinColumn(name = "approved_by", referencedColumnName = "user_id")
    private User approvedBy;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String remarks;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTreasuryTxnId() {
        return treasuryTxnId;
    }

    public void setTreasuryTxnId(String treasuryTxnId) {
        this.treasuryTxnId = treasuryTxnId;
    }

    public String getSystemAccountId() {
        return systemAccountId;
    }

    public void setSystemAccountId(String systemAccountId) {
        this.systemAccountId = systemAccountId;
    }

    public TreasuryTxnType getTxnType() {
        return txnType;
    }

    public void setTxnType(TreasuryTxnType txnType) {
        this.txnType = txnType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}