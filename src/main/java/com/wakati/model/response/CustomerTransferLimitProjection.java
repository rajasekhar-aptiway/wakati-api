package com.wakati.model.response;

import java.math.BigDecimal;

public interface CustomerTransferLimitProjection {

    String getCustomerUserId();   // customer.userId

    String getCustomerName();     // customer.fullName

    BigDecimal getDailyTransferLimit();

    BigDecimal getSingleTransferLimit();

    String getSetByUserId();
}