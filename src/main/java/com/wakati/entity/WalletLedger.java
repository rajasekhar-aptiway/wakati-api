package com.wakati.entity;

import com.wakati.enums.EntryType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "WALLET_LEDGER")
public class WalletLedger extends BaseCreatedAtEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ledger_id")
    private String ledgerId;

    @ManyToOne
    @JoinColumn(name = "txn_id", referencedColumnName = "txn_id")
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    private String amount;
    private String balanceAfter;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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



    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(String balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

}