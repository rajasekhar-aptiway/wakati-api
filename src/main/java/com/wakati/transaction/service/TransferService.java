package com.wakati.transaction.service;

import com.wakati.entity.Wallet;
import com.wakati.exception.WakatiException;
import com.wakati.model.response.CommissionResult;
import com.wakati.repository.WalletRepository;
import com.wakati.transaction.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static com.wakati.I18NConstants.INSUFFICIENT_BALANCE;

@Service
public class TransferService {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private LedgerService ledgerService;

    @Autowired
    private CommissionService commissionService;

    @Transactional
    public Map<String, Object> handleTransfer(TransactionRequest req) {

        Wallet source = walletRepository.lockByOwnerId(req.getSourceUserId());
        Wallet target = walletRepository.lockByOwnerId(req.getTargetUserId());

        CommissionResult commission = commissionService.calculate(req);

        BigDecimal totalCommission = commission.getTotalCommission();
        //double platformShare = commission.getPlatformShare();
        //double remaining = commission.getRemainingShare();

        BigDecimal totalAmountWithCommission = req.getAmount().add(totalCommission);
        if ((source.getBalance().compareTo(totalAmountWithCommission) < 0)) {
            throw new WakatiException(INSUFFICIENT_BALANCE);
        }

        source.setBalance(source.getBalance().subtract(totalAmountWithCommission));
        target.setBalance(target.getBalance().add(req.getAmount()));

        walletRepository.save(source);
        walletRepository.save(target);

        ledgerService.walletDebit(source, req.getAmount(),req.getReferenceId());
        ledgerService.walletCredit(target, req.getAmount(),req.getReferenceId());

        return Map.of("code", 200, "message", "Transfer successful");
    }


}