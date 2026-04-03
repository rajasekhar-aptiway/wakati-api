package com.wakati.transaction.service;

import com.wakati.entity.CommissionConfiguration;
import com.wakati.exception.WakatiException;
import com.wakati.model.response.CommissionResult;
import com.wakati.repository.CommissionConfigurationRepository;
import com.wakati.transaction.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.wakati.I18NConstants.COMMISSION_CONFIG_NOT_FOUND;

@Service
public class CommissionService {

    @Autowired
    private CommissionConfigurationRepository repository;

    public CommissionResult calculate(TransactionRequest req) {

        CommissionConfiguration config = repository
                .findByTxnTypeAndChannelAndStatus(
                        req.getTxnType(),
                        req.getChannel(),
                        "ACTIVE"
                )
                .orElseThrow(() -> new WakatiException(COMMISSION_CONFIG_NOT_FOUND));

        BigDecimal amount = req.getAmount();

        BigDecimal totalCommission = amount.multiply(
                config.getPercent()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        );

        BigDecimal platformShare = totalCommission.multiply(
                config.getPlatformPercent()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        );

        BigDecimal remaining = totalCommission.subtract(platformShare);

        return new CommissionResult(
                totalCommission,
                platformShare,
                remaining
        );
    }
}