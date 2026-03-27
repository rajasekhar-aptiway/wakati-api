package com.wakati.repository;

import com.wakati.entity.TreasuryActions;
import com.wakati.enums.TreasuryTxnType;
import com.wakati.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TreasuryActionsRepository extends JpaRepository<TreasuryActions, Integer> {

    Optional<TreasuryActions> findByTreasuryTxnId(String txnId);

    List<TreasuryActions> findByStatus(VerificationStatus status);

    List<TreasuryActions> findByTxnType(TreasuryTxnType txnType);

    List<TreasuryActions> findByCreatedAtBetween(Date start, Date end);
}