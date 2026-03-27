package com.wakati.entity;

import com.wakati.enums.CollectionRequestStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "COLLECTION_REQUEST")
public class CollectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String collectionRequestId;
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

    @ManyToOne
    @JoinColumn(name = "partner_agent_id", referencedColumnName = "user_id")
    private User partnerAgent;

    private String island;

    @Enumerated(EnumType.STRING)
    private CollectionRequestStatus status;

    private Double amountCollected;

    private LocalDateTime otpVerifiedAtDealer;
    private LocalDateTime otpVerifiedAtSd;
    private LocalDateTime otpVerifiedAtPartnerAgent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollectionRequestId() {
        return collectionRequestId;
    }

    public void setCollectionRequestId(String collectionRequestId) {
        this.collectionRequestId = collectionRequestId;
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

    public User getPartnerAgent() {
        return partnerAgent;
    }

    public void setPartnerAgent(User partnerAgent) {
        this.partnerAgent = partnerAgent;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public CollectionRequestStatus getStatus() {
        return status;
    }

    public void setStatus(CollectionRequestStatus status) {
        this.status = status;
    }

    public Double getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(Double amountCollected) {
        this.amountCollected = amountCollected;
    }

    public LocalDateTime getOtpVerifiedAtDealer() {
        return otpVerifiedAtDealer;
    }

    public void setOtpVerifiedAtDealer(LocalDateTime otpVerifiedAtDealer) {
        this.otpVerifiedAtDealer = otpVerifiedAtDealer;
    }

    public LocalDateTime getOtpVerifiedAtSd() {
        return otpVerifiedAtSd;
    }

    public void setOtpVerifiedAtSd(LocalDateTime otpVerifiedAtSd) {
        this.otpVerifiedAtSd = otpVerifiedAtSd;
    }

    public LocalDateTime getOtpVerifiedAtPartnerAgent() {
        return otpVerifiedAtPartnerAgent;
    }

    public void setOtpVerifiedAtPartnerAgent(LocalDateTime otpVerifiedAtPartnerAgent) {
        this.otpVerifiedAtPartnerAgent = otpVerifiedAtPartnerAgent;
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

    // Getters and Setters
}