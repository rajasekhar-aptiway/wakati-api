package com.wakati.entity;

import jakarta.persistence.*;
import com.wakati.enums.RequestedByType;
import com.wakati.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMER_TRANSFER_LIMITS")
public class CustomerTransferLimits extends BaseUpdatedAtEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id")
    private User customer;

    private BigDecimal dailyTransferLimit;
    private BigDecimal singleTransferLimit;

    @ManyToOne
    @JoinColumn(name = "set_by", referencedColumnName = "user_id")
    private User setBy;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public BigDecimal getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(BigDecimal dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public BigDecimal getSingleTransferLimit() {
        return singleTransferLimit;
    }

    public void setSingleTransferLimit(BigDecimal singleTransferLimit) {
        this.singleTransferLimit = singleTransferLimit;
    }

    public User getSetBy() {
        return setBy;
    }

    public void setSetBy(User setBy) {
        this.setBy = setBy;
    }

}