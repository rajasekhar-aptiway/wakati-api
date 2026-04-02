package com.wakati.transaction.model;

import com.wakati.model.response.TransactionProjection;
import com.wakati.model.response.TransactionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionMapper {

    public TransactionResponse toDto(TransactionProjection p, String userId) {

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

        // 🧠 Entry override
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