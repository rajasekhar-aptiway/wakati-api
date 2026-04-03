package com.wakati.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DEALER_CUMULATIVE_STATS")
public class DealerCumulativeStats extends BaseUpdatedAtEntity{

    @Id
    @OneToOne
    @JoinColumn(name = "dealer_id", referencedColumnName = "user_id")
    private User dealer;

    private BigDecimal cumulativeDeposited;
    private BigDecimal cumulativeWithdrawn;
    private BigDecimal dailyDeposited;
    private BigDecimal dailyWithdrawn;

    private LocalDate dailyResetDate;



    public User getDealer() {
        return dealer;
    }

    public void setDealer(User dealer) {
        this.dealer = dealer;
    }

    public BigDecimal getCumulativeDeposited() {
        return cumulativeDeposited;
    }

    public void setCumulativeDeposited(BigDecimal cumulativeDeposited) {
        this.cumulativeDeposited = cumulativeDeposited;
    }

    public BigDecimal getCumulativeWithdrawn() {
        return cumulativeWithdrawn;
    }

    public void setCumulativeWithdrawn(BigDecimal cumulativeWithdrawn) {
        this.cumulativeWithdrawn = cumulativeWithdrawn;
    }

    public BigDecimal getDailyDeposited() {
        return dailyDeposited;
    }

    public void setDailyDeposited(BigDecimal dailyDeposited) {
        this.dailyDeposited = dailyDeposited;
    }

    public BigDecimal getDailyWithdrawn() {
        return dailyWithdrawn;
    }

    public void setDailyWithdrawn(BigDecimal dailyWithdrawn) {
        this.dailyWithdrawn = dailyWithdrawn;
    }

    public LocalDate getDailyResetDate() {
        return dailyResetDate;
    }

    public void setDailyResetDate(LocalDate dailyResetDate) {
        this.dailyResetDate = dailyResetDate;
    }

}
