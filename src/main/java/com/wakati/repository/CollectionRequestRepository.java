package com.wakati.repository;

import com.wakati.entity.CollectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRequestRepository extends JpaRepository<CollectionRequest, Long> {

    Optional<CollectionRequest> findById(Long id);

    Optional<CollectionRequest> findByCollectionRequestId(String collectionRequestId);

    Optional<CollectionRequest> findByNotificationId(String notificationId);

    List<CollectionRequest> findByNotificationType(String notificationType);

    Optional<CollectionRequest> findByDealerId(String dealerId);

    Optional<CollectionRequest> findBySuperDealerId(String superDealerId);

    Optional<CollectionRequest> findByReceiverId(String receiverId);

    Optional<CollectionRequest> findByPartnerAgentId(String partnerAgentId);

    List<CollectionRequest> findByStatus(String status);
}
