package com.wakati.entity;

import com.wakati.enums.FromLocation;
import com.wakati.enums.ToLocation;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CASH_TRANSACTION_LEDGER")
public class CashTransactionLedger extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cashTxnId;

    @Enumerated(EnumType.STRING)
    private FromLocation fromLocation;

    @ManyToOne
    @JoinColumn(name = "from_id", referencedColumnName = "user_id")
    private User fromUser;

    @Enumerated(EnumType.STRING)
    private ToLocation toLocation;

    @ManyToOne
    @JoinColumn(name = "to_id", referencedColumnName = "user_id")
    private User toUser;

    private String amount;
    private String targetBalanceAfter;
    private String sourceBalanceAfter;

    private String reasonCode;
    private String referenceId;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCashTxnId() {
        return cashTxnId;
    }

    public void setCashTxnId(String cashTxnId) {
        this.cashTxnId = cashTxnId;
    }

    public FromLocation getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(FromLocation fromLocation) {
        this.fromLocation = fromLocation;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public ToLocation getToLocation() {
        return toLocation;
    }

    public void setToLocation(ToLocation toLocation) {
        this.toLocation = toLocation;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTargetBalanceAfter() {
        return targetBalanceAfter;
    }

    public void setTargetBalanceAfter(String targetBalanceAfter) {
        this.targetBalanceAfter = targetBalanceAfter;
    }

    public String getSourceBalanceAfter() {
        return sourceBalanceAfter;
    }

    public void setSourceBalanceAfter(String sourceBalanceAfter) {
        this.sourceBalanceAfter = sourceBalanceAfter;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

}