package com.wakati.repository;

import com.wakati.entity.PayRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayRequestRepository extends JpaRepository<PayRequest, Long> {

    public Optional<PayRequest> findByPayRequestId(String payRequestId);

    public List<PayRequest> findByDealerId(String dealerId);

    public List<PayRequest> findByReceiverId(String receiverId);

    public List<PayRequest> findByStatus(String status);
}
