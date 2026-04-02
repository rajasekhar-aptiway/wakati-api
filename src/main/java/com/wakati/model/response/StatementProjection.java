package com.wakati.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StatementProjection {

    String getTxnId();
    String getDirection();

    BigDecimal getAmount();
    BigDecimal getBalanceAfter();

    LocalDateTime getCreatedAt();
    String getTxnType();

    String getSourceUserId();
    String getTargetUserId();

    String getSourceName();
    String getSourceMobile();

    String getTargetName();
    String getTargetMobile();

    BigDecimal getCommissionAmount();
    Integer getCommissionInAmount();
}