package com.wakati.repository;

import com.wakati.entity.Cash;
import com.wakati.entity.CashLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashLedgerRepository extends JpaRepository<CashLedger, Long> {

        Optional<CashLedger> findByLedgerId(String ledgerId);

        Optional<CashLedger> findByTransaction(String txnId);

        Optional<CashLedger> findByUserId(String userId);

        List<CashLedger> findByEntryType(String entryType);

}
