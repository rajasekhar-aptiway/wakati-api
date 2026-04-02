package com.wakati.transaction.service;

import com.wakati.entity.Transaction;
import com.wakati.entity.User;
import com.wakati.entity.Wallet;
import com.wakati.entity.WalletLedger;
import com.wakati.enums.EntryType;
import com.wakati.repository.WalletLedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LedgerService {

    @Autowired
    private WalletLedgerRepository ledgerRepository;

    public void walletDebit(Wallet wallet, BigDecimal amount, String txnId) {

        BigDecimal balanceAfter = wallet.getBalance(); // already deducted

        WalletLedger ledger = new WalletLedger();
        ledger.setLedgerId(UUID.randomUUID().toString());
        Transaction transaction = new Transaction();
        transaction.setTxnId(txnId);
        ledger.setTransaction(transaction);
        ledger.setUser(wallet.getOwner());
        ledger.setEntryType(EntryType.DEBIT);
        ledger.setAmount(amount);
        ledger.setBalanceAfter(balanceAfter);
        ledger.setCreatedAt(LocalDateTime.now());

        ledgerRepository.save(ledger);
    }

    public void walletCredit(Wallet wallet, BigDecimal amount, String txnId) {

        BigDecimal balanceAfter = wallet.getBalance(); // already added

        WalletLedger ledger = new WalletLedger();
        ledger.setLedgerId(UUID.randomUUID().toString());
        Transaction transaction = new Transaction();
        transaction.setTxnId(txnId);
        ledger.setTransaction(transaction);
        ledger.setUser(wallet.getOwner());
        ledger.setEntryType(EntryType.CREDIT);
        ledger.setAmount(amount);
        ledger.setBalanceAfter(balanceAfter);
        ledger.setCreatedAt(LocalDateTime.now());

        ledgerRepository.save(ledger);
    }
}