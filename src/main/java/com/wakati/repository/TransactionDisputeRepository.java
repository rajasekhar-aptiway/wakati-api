package com.wakati.repository;

import com.wakati.entity.TransactionDispute;
import com.wakati.enums.DisputeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDisputeRepository extends JpaRepository<TransactionDispute, String> {

    List<TransactionDispute> findByTransaction(String txnId);

    List<TransactionDispute> findByRaisedBy(String userId);

    List<TransactionDispute> findByStatus(DisputeStatus status);
}