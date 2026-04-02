package com.wakati.repository;

import com.wakati.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("""
        SELECT COUNT(n)
        FROM Notification n
        WHERE 
        (
            n.target.userId = :userId
            OR n.partnerAgent.userId = :userId
            OR n.superDealer.userId = :userId
            OR n.dealer.userId = :userId
        )
        AND (n.workflowStatus IS NULL OR n.workflowStatus NOT IN ('COMPLETED','SUPERSEDED'))
    """)
    Long countRunning(String userId);

}