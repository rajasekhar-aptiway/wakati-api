package com.wakati.transaction.service;

import com.wakati.entity.Transaction;
import com.wakati.entity.User;
import com.wakati.entity.Wallet;
import com.wakati.enums.TransactionType;
import com.wakati.enums.UserType;
import com.wakati.exception.WakatiException;
import com.wakati.repository.CashRepository;
import com.wakati.repository.DealerSecurityDepositsRepository;
import com.wakati.repository.TransactionRepository;
import com.wakati.repository.WalletRepository;
import com.wakati.service.UserService;
import com.wakati.transaction.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static com.wakati.I18NConstants.*;

@Service
public class SecurityDepositService {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DealerSecurityDepositsRepository securityDepositsRepository;

    @Transactional
    public Map<String, Object> handle(TransactionRequest req) {

        String dealerId = req.getTargetUserId();
        User user = userService.getUserByUserId(dealerId);
        if (!UserType.DEALER.name().equals(user.getUserType().name())) {
            throw new WakatiException(INVALID_DEALER);
        }

        if (securityDepositsRepository.hasSecurityDeposit(dealerId)) {
            throw new WakatiException(DEPOSIT_ALREADY_EXISTS);
        }

        Wallet wallet = walletRepository.findByOwnerIdForUpdate(dealerId)
                .orElseThrow(() -> new WakatiException(WALLET_NOT_FOUND));

        BigDecimal newBalance = wallet.getBalance().add(req.getOpeningBalance());

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        // Insert transaction
        Transaction txn = new Transaction();
        txn.setTxnId(UUID.randomUUID().toString());
        txn.setTxnType(TransactionType.SECURITY_DEPOSIT);
        txn.setAmount(req.getAmount());
        txn.setStatus("COMPLETED");

        transactionRepository.save(txn);

        return Map.of(
                "code", 200,
                "message", "Security deposit added",
                "txnId", txn.getTxnId()
        );
    }
}