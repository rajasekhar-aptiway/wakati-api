package com.wakati.repository;

import com.wakati.entity.CashTransactionLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashTransactionLedgerRepository extends JpaRepository<CashTransactionLedger, Long> {

    Optional<CashTransactionLedger> findById(Long id);

    Optional<CashTransactionLedger> findByCashTxnId(String cashTxnId);

    Optional<CashTransactionLedger> findByReferenceId(String referenceId);

    Optional<CashTransactionLedger> findByFromUser(String fromId);

    Optional<CashTransactionLedger> findByToUser(String toId);

    List<CashTransactionLedger> findByFromLocation(String fromLocation);

    List<CashTransactionLedger> findByToLocation(String toLocation);
}
