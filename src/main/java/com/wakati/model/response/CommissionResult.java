package com.wakati.model.response;

import java.math.BigDecimal;

public class CommissionResult {

    private final BigDecimal totalCommission;
    private final BigDecimal platformShare;
    private final BigDecimal remainingShare;

    public CommissionResult(BigDecimal totalCommission, BigDecimal platformShare, BigDecimal remainingShare) {
        this.totalCommission = totalCommission;
        this.platformShare = platformShare;
        this.remainingShare = remainingShare;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public BigDecimal getPlatformShare() {
        return platformShare;
    }

    public BigDecimal getRemainingShare() {
        return remainingShare;
    }
}