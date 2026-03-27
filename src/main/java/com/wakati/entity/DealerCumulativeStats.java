package com.wakati.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DEALER_CUMULATIVE_STATS")
public class DealerCumulativeStats {

    @Id
    @OneToOne
    @JoinColumn(name = "dealer_id", referencedColumnName = "user_id")
    private User dealer;

    private Double cumulativeDeposited;
    private Double cumulativeWithdrawn;
    private Double dailyDeposited;
    private Double dailyWithdrawn;

    private LocalDate dailyResetDate;

    private LocalDateTime updatedAt;

    public User getDealer() {
        return dealer;
    }

    public void setDealer(User dealer) {
        this.dealer = dealer;
    }

    public Double getCumulativeDeposited() {
        return cumulativeDeposited;
    }

    public void setCumulativeDeposited(Double cumulativeDeposited) {
        this.cumulativeDeposited = cumulativeDeposited;
    }

    public Double getCumulativeWithdrawn() {
        return cumulativeWithdrawn;
    }

    public void setCumulativeWithdrawn(Double cumulativeWithdrawn) {
        this.cumulativeWithdrawn = cumulativeWithdrawn;
    }

    public Double getDailyDeposited() {
        return dailyDeposited;
    }

    public void setDailyDeposited(Double dailyDeposited) {
        this.dailyDeposited = dailyDeposited;
    }

    public Double getDailyWithdrawn() {
        return dailyWithdrawn;
    }

    public void setDailyWithdrawn(Double dailyWithdrawn) {
        this.dailyWithdrawn = dailyWithdrawn;
    }

    public LocalDate getDailyResetDate() {
        return dailyResetDate;
    }

    public void setDailyResetDate(LocalDate dailyResetDate) {
        this.dailyResetDate = dailyResetDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
