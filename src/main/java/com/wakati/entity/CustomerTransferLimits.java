package com.wakati.entity;

import jakarta.persistence.*;
import com.wakati.enums.RequestedByType;
import com.wakati.enums.Status;

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

    private Double dailyTransferLimit;
    private Double singleTransferLimit;

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

    public Double getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(Double dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public Double getSingleTransferLimit() {
        return singleTransferLimit;
    }

    public void setSingleTransferLimit(Double singleTransferLimit) {
        this.singleTransferLimit = singleTransferLimit;
    }

    public User getSetBy() {
        return setBy;
    }

    public void setSetBy(User setBy) {
        this.setBy = setBy;
    }

}