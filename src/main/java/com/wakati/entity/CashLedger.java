package com.wakati.entity;

import com.wakati.enums.EntryType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CASH_LEDGER")
public class CashLedger extends BaseCreatedAtEntity {

    @Id
    private String ledgerId;

    @ManyToOne
    @JoinColumn(name = "txn_id", referencedColumnName = "txn_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    private Double amount;
    private Double balanceAfter;



    public String getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

}