package com.wakati.entity;

import com.wakati.enums.OwnerType;
import com.wakati.enums.WalletStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "WALLET")
public class Wallet extends BaseUpdatedAtEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "wallet_id")
    private String walletId;

    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private User owner;

    private String balance;

    @Enumerated(EnumType.STRING)
    private WalletStatus status;



    @OneToMany(mappedBy = "ledgerId")
    private List<WalletLedger> ledgers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }



    public List<WalletLedger> getLedgers() {
        return ledgers;
    }

    public void setLedgers(List<WalletLedger> ledgers) {
        this.ledgers = ledgers;
    }
}