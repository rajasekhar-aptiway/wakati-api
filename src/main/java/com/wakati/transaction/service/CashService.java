package com.wakati.transaction.service;

import com.wakati.entity.Cash;
import com.wakati.entity.Wallet;
import com.wakati.exception.WakatiException;
import com.wakati.repository.CashRepository;
import com.wakati.repository.WalletRepository;
import com.wakati.transaction.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.wakati.I18NConstants.INSUFFICIENT_WALLET_BALANCE;

@Service
public class CashService {

    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public Map<String, Object> handleCash(TransactionRequest req) {

        Cash cash = cashRepository.lockByUserId(req.getSourceUserId());
        Wallet wallet = walletRepository.lockByOwnerId(req.getTargetUserId());

        if ("CASH_DEPOSIT".equals(req.getTxnType())) {

            cash.setBalance(cash.getBalance().add(req.getAmount()));
            wallet.setBalance(wallet.getBalance().add(req.getAmount()));

        } else {

            if (wallet.getBalance().compareTo(req.getAmount()) < 0) {
                throw new WakatiException(INSUFFICIENT_WALLET_BALANCE);
            }

            cash.setBalance(cash.getBalance().subtract(req.getAmount()));
            wallet.setBalance(wallet.getBalance().subtract(req.getAmount()));
        }

        return Map.of("code", 200, "message", "Cash transaction successful");
    }
}