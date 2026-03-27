package com.wakati.repository;

import com.wakati.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    public Optional<Notification> findByNotificationId(String notificationId);

    public List<Notification> findBySourceId(String sourceId);

    public List<Notification> findByTargetId(String targetId);

    public List<Notification> findBySourceIdAndTargetId(String sourceId, String targetId);

    public List<Notification> findByNotificationType(String notificationType);

    public List<Notification> findByPaymentMode(String paymentMode);

    public List<Notification> findByDealerId(String dealerId);

    public List<Notification> findBySuperDealerId(String superDealerId);

    public List<Notification> findByReceiverId(String receiverId);

    public List<Notification> findByPartnerAgentId(String partnerAgentId);

    public Optional<Notification> findByPayRequestId(String payRequestId);

    public Optional<Notification> findByCollectionRequestId(String collectionRequestId);

}