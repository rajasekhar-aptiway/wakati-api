package com.wakati.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification extends BaseUpdatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String notificationId;

    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "user_id")
    private User source;

    @ManyToOne
    @JoinColumn(name = "target_id", referencedColumnName = "user_id")
    private User target;

    private String paymentMode;
    private String type;
    private String notificationType;

    @ManyToOne
    @JoinColumn(name = "dealer_id", referencedColumnName = "user_id")
    private User dealer;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "super_dealer_id", referencedColumnName = "user_id")
    private User superDealer;

    @ManyToOne
    @JoinColumn(name = "partner_agent_id", referencedColumnName = "user_id")
    private User partnerAgent;

    private String island;
    private String workflowStatus;
    private String amountCollected;

    private String payRequestId;
    private String collectionRequestId;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public User getSource() {
        return source;
    }

    public void setSource(User source) {
        this.source = source;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSuperDealer() {
        return superDealer;
    }

    public void setSuperDealer(User superDealer) {
        this.superDealer = superDealer;
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

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(String workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public String getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(String amountCollected) {
        this.amountCollected = amountCollected;
    }

    public String getPayRequestId() {
        return payRequestId;
    }

    public void setPayRequestId(String payRequestId) {
        this.payRequestId = payRequestId;
    }

    public String getCollectionRequestId() {
        return collectionRequestId;
    }

    public void setCollectionRequestId(String collectionRequestId) {
        this.collectionRequestId = collectionRequestId;
    }

}