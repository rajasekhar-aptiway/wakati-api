package com.wakati.transaction.service;

import com.wakati.entity.User;
import com.wakati.enums.UserType;
import com.wakati.exception.WakatiException;
import com.wakati.model.response.TransactionProjection;
import com.wakati.model.response.TransactionResponse;
import com.wakati.repository.TransactionRepository;
import com.wakati.repository.UserRepository;
import com.wakati.transaction.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.wakati.I18NConstants.*;

@Service
public class TransactionService {

    @Autowired
    private SecurityDepositService securityDepositService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private CashService cashService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Map<String, Object> process(TransactionRequest req) {

        validate(req);

        if ("SECURITY_DEPOSIT".equals(req.getSecurityDeposit())) {
            return securityDepositService.handle(req);
        }

        if ("TRANSFER".equals(req.getTxnType())) {
            return transferService.handleTransfer(req);
        }

        if ("CASH_DEPOSIT".equals(req.getTxnType()) ||
            "CASH_WITHDRAWAL".equals(req.getTxnType())) {

            return cashService.handleCash(req);
        }

        throw new WakatiException(INVALID_TXN_TYPE);
    }

    public Map<String, Object> getTransactions(String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new WakatiException(USER_NOT_FOUND));

        List<TransactionProjection> ledger;

        if (isCashUser(user.getUserType())) {
            ledger = transactionRepository.fetchCashLedger(userId);
        } else {
            ledger = transactionRepository.fetchWalletLedger(userId);
        }

        List<TransactionProjection> commission =
                transactionRepository.fetchCommission(userId);

        List<TransactionProjection> all = new ArrayList<>();
        all.addAll(ledger);
        all.addAll(commission);

        // ✅ Sort like ORDER BY created_at DESC
        all.sort(Comparator.comparing(TransactionProjection::getCreatedAt).reversed());

        List<TransactionResponse> response =
                all.stream().map(p -> mapProjection(p, userId)).toList();

        return Map.of(
                "code", 200,
                "message", "Transaction list fetched successfully",
                "count", response.size(),
                "data", response
        );
    }

    private void validate(TransactionRequest req) {
        if (req.getSourceUserId().equals(req.getTargetUserId())) {
            throw new WakatiException(SAME_ACCOUNT_NOT_ALLOWED);
        }
        if (req.getAmount() == null || (req.getAmount().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new WakatiException(INVALID_AMOUNT);
        }
    }

    private boolean isCashUser(UserType type) {
        return List.of(
                UserType.DEALER,
                UserType.FRONT_DESK,
                UserType.RECEIVER,
                UserType.SUPER_DEALER,
                UserType.PARTNER_AGENT
        ).contains(type);
    }

    private TransactionResponse mapProjection(TransactionProjection p, String userId) {

        String entryType = p.getEntryType();
        BigDecimal amount = p.getAmount();
        BigDecimal balance = p.getBalanceAfter() != null
                ? p.getBalanceAfter()
                : BigDecimal.ZERO;

        String txnType = p.getTxnType();

        String sourceId = p.getSourceUserId();
        String targetId = p.getTargetUserId();

        // 🧠 Direction logic (same as PHP)
        String direction;

        if ("COMMISSION".equals(txnType) || "CASH_DEPOSIT".equals(txnType)) {
            direction = "IN";
        } else if ("CASH_WITHDRAWAL".equals(txnType)) {
            direction = "OUT";
        } else if (userId.equals(sourceId)) {
            direction = "OUT";
        } else if (userId.equals(targetId)) {
            direction = "IN";
        } else {
            direction = "CREDIT".equals(entryType) ? "IN" : "OUT";
        }

        // 🧠 Entry type override
        if ("CASH_DEPOSIT".equals(txnType)) {
            entryType = "CREDIT";
        }
        if ("CASH_WITHDRAWAL".equals(txnType)) {
            entryType = "DEBIT";
        }

        return TransactionResponse.builder()
                .transactionId(p.getTxnId())
                .category(p.getTxnCategory())
                .type(p.getTxnType())
                .channel(p.getChannel())
                .initiatedBy(p.getInitiatedBy())
                .direction(direction)

                .senderUserId(sourceId)
                .senderName(p.getSourceName() != null ? p.getSourceName() : "SYSTEM")
                .senderMobile(p.getSourceMobile() != null ? p.getSourceMobile() : "")

                .receiverUserId(targetId)
                .receiverName(p.getTargetName() != null ? p.getTargetName() : "SYSTEM")
                .receiverMobile(p.getTargetMobile() != null ? p.getTargetMobile() : "")

                .transactionAmount(amount)
                .ledgerEntryType(entryType)
                .remainingBalance(balance)

                .createdAt(p.getCreatedAt())
                .remarks(p.getRemarks() != null ? p.getRemarks() : "System transaction")
                .build();
    }
}