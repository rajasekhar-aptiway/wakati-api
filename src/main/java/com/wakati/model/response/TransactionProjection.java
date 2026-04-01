package com.wakati.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionProjection {

    String getTxnId();
    String getEntryType();

    BigDecimal getAmount();
    BigDecimal getBalanceAfter();

    LocalDateTime getCreatedAt();

    String getTxnType();
    String getTxnCategory();
    String getChannel();
    String getInitiatedBy();

    String getSourceUserId();
    String getTargetUserId();

    String getRemarks();

    String getSourceName();
    String getSourceMobile();

    String getTargetName();
    String getTargetMobile();
}