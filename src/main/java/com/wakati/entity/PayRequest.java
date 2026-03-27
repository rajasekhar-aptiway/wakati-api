package com.wakati.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PAY_REQUEST")
public class PayRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String payRequestId;
    private String notificationId;
    private String notificationType;

    @ManyToOne
    @JoinColumn(name = "dealer_id", referencedColumnName = "user_id")
    private User dealer;

    @ManyToOne
    @JoinColumn(name = "super_dealer_id", referencedColumnName = "user_id")
    private User superDealer;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    private String island;

    private String status;

    private Double amountToPay;

    private LocalDateTime otpVerifiedAtReceiverToSd;
    private LocalDateTime otpVerifiedAtSdToDealer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "partner_agent_id", referencedColumnName = "user_id")
    private User partnerAgent;

    private LocalDateTime otpVerifiedAtReceiverToPa;
    private LocalDateTime otpVerifiedAtPaToSd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPayRequestId() {
        return payRequestId;
    }

    public void setPayRequestId(String payRequestId) {
        this.payRequestId = payRequestId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public User getDealer() {
        return dealer;
    }

    public void setDealer(User dealer) {
        this.dealer = dealer;
    }

    public User getSuperDealer() {
        return superDealer;
    }

    public void setSuperDealer(User superDealer) {
        this.superDealer = superDealer;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public LocalDateTime getOtpVerifiedAtReceiverToSd() {
        return otpVerifiedAtReceiverToSd;
    }

    public void setOtpVerifiedAtReceiverToSd(LocalDateTime otpVerifiedAtReceiverToSd) {
        this.otpVerifiedAtReceiverToSd = otpVerifiedAtReceiverToSd;
    }

    public LocalDateTime getOtpVerifiedAtSdToDealer() {
        return otpVerifiedAtSdToDealer;
    }

    public void setOtpVerifiedAtSdToDealer(LocalDateTime otpVerifiedAtSdToDealer) {
        this.otpVerifiedAtSdToDealer = otpVerifiedAtSdToDealer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getPartnerAgent() {
        return partnerAgent;
    }

    public void setPartnerAgent(User partnerAgent) {
        this.partnerAgent = partnerAgent;
    }

    public LocalDateTime getOtpVerifiedAtReceiverToPa() {
        return otpVerifiedAtReceiverToPa;
    }

    public void setOtpVerifiedAtReceiverToPa(LocalDateTime otpVerifiedAtReceiverToPa) {
        this.otpVerifiedAtReceiverToPa = otpVerifiedAtReceiverToPa;
    }

    public LocalDateTime getOtpVerifiedAtPaToSd() {
        return otpVerifiedAtPaToSd;
    }

    public void setOtpVerifiedAtPaToSd(LocalDateTime otpVerifiedAtPaToSd) {
        this.otpVerifiedAtPaToSd = otpVerifiedAtPaToSd;
    }

    // Getters and Setters
}