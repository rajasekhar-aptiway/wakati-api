package com.wakati.repository;

import com.wakati.entity.Transaction;
import com.wakati.enums.TransactionChannel;
import com.wakati.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Optional<Transaction> findByTxnId(String txnId);

    List<Transaction> findBySourceUserId(String userId);

    List<Transaction> findByTargetUserId(String userId);

    List<Transaction> findByChannel(TransactionChannel channel);

    List<Transaction> findByTxnType(TransactionType txnType);

    List<Transaction> findByCreatedAtBetween(Date start, Date end);

    List<Transaction> findByBatchId(String batchId);

//    @Query("""
//        SELECT COALESCE(SUM(t.amount),0)
//        FROM Transaction t
//        WHERE t.txnType = :type
//        AND t.sourceUserId = :userId
//        AND DATE(t.createdAt) = CURRENT_DATE
//    """)
//    Double getTodayAmount(@Param("type") String type,
//                          @Param("userId") String userId);
//
//    @Query(value = """
//    SELECT COALESCE(SUM(wl.amount), 0)
//    FROM WALLET_LEDGER wl
//    JOIN TRANSACTIONS t ON t.txn_id = wl.txn_id
//    WHERE wl.user_id = :userId
//    AND wl.entry_type = 'CREDIT'
//    AND t.txn_type = 'COMMISSION'
//    AND DATE(wl.created_at) = CURDATE()
//""", nativeQuery = true)
//    Double getDealerTodayCommission(@Param("userId") String userId);
}