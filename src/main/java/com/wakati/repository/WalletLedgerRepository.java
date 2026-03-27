package com.wakati.repository;

import com.wakati.entity.WalletLedger;
import com.wakati.enums.EntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalletLedgerRepository extends JpaRepository<WalletLedger, Integer> {

    List<WalletLedger> findByUser(String user);

    List<WalletLedger> findByTransaction(String transaction);

    List<WalletLedger> findByEntryType(EntryType entryType);

    List<WalletLedger> findByUserIdOrderByCreatedAtDesc(String userId);

//    @Query("""
//    SELECT COALESCE(SUM(wl.amount), 0)
//    FROM WalletLedger wl
//    WHERE wl.userId = :userId
//    AND wl.entryType = 'CREDIT'
//    AND wl.transaction.txnType = 'COMMISSION'
//    AND wl.createdAt BETWEEN :start AND :end
//""")
//    Double getDealerTodayCommission(
//            @Param("userId") String userId,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );

}