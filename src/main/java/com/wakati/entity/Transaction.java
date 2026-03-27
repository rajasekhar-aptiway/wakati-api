package com.wakati.entity;

import com.wakati.enums.Channel;
import com.wakati.enums.TransactionType;
import com.wakati.enums.TxnCategory;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "txn_id")
    private String txnId;

    @Enumerated(EnumType.STRING)
    private TxnCategory txnCategory;

    @Enumerated(EnumType.STRING)
    private TransactionType txnType;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "initiated_by", referencedColumnName = "user_id")
    private User initiatedBy;

    @ManyToOne
    @JoinColumn(name = "source_user_id", referencedColumnName = "user_id")
    private User sourceUser;

    @ManyToOne
    @JoinColumn(name = "target_user_id", referencedColumnName = "user_id")
    private User targetUser;

    private String amount;
    private String remarks;
    private String parentTxnId;
    private String status;
    private String batchId;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionDispute> disputes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public TxnCategory getTxnCategory() {
        return txnCategory;
    }

    public void setTxnCategory(TxnCategory txnCategory) {
        this.txnCategory = txnCategory;
    }

    public TransactionType getTxnType() {
        return txnType;
    }

    public void setTxnType(TransactionType txnType) {
        this.txnType = txnType;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public User getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(User initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public User getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(User sourceUser) {
        this.sourceUser = sourceUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getParentTxnId() {
        return parentTxnId;
    }

    public void setParentTxnId(String parentTxnId) {
        this.parentTxnId = parentTxnId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<TransactionDispute> getDisputes() {
        return disputes;
    }

    public void setDisputes(List<TransactionDispute> disputes) {
        this.disputes = disputes;
    }
}