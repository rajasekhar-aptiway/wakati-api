package com.wakati.repository;

import com.wakati.entity.Transaction;
import com.wakati.enums.TransactionChannel;
import com.wakati.enums.TransactionType;
import com.wakati.model.response.TransactionProjection;
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

    @Query("""
    SELECT 
        t.txnId AS txnId,
        'CREDIT' AS entryType,
        t.amount AS amount,
        NULL AS balanceAfter,
        t.createdAt AS createdAt,

        t.txnType AS txnType,
        t.txnCategory AS txnCategory,
        t.channel AS channel,
        t.initiatedBy AS initiatedBy,
        t.sourceUser.userId AS sourceUserId,
        t.targetUser.userId AS targetUserId,
        t.remarks AS remarks,

        su.fullName AS sourceName,
        su.mobileNo AS sourceMobile,
        tu.fullName AS targetName,
        tu.mobileNo AS targetMobile

    FROM Transaction t
    LEFT JOIN t.sourceUser su
    LEFT JOIN t.targetUser tu

    WHERE t.txnType = 'COMMISSION'
    AND t.sourceUser.userId = :userId
""")
    List<TransactionProjection> fetchCommission(@Param("userId") String userId);

    @Query("""
    SELECT 
        wl.transaction.txnId AS txnId,
        wl.entryType AS entryType,
        wl.amount AS amount,
        wl.balanceAfter AS balanceAfter,
        wl.createdAt AS createdAt,

        t.txnType AS txnType,
        t.txnCategory AS txnCategory,
        t.channel AS channel,
        t.initiatedBy AS initiatedBy,
        t.sourceUser.userId AS sourceUserId,
        t.targetUser.userId AS targetUserId,
        t.remarks AS remarks,

        su.fullName AS sourceName,
        su.mobileNo AS sourceMobile,
        tu.fullName AS targetName,
        tu.mobileNo AS targetMobile

    FROM WalletLedger wl
    LEFT JOIN Transaction t ON wl.transaction.txnId = t.txnId
    LEFT JOIN t.sourceUser su
    LEFT JOIN t.targetUser tu

    WHERE wl.user.userId = :userId
""")
    List<TransactionProjection> fetchWalletLedger(@Param("userId") String userId);

    @Query("""
    SELECT
     cl.transaction.txnId AS txnId,
        cl.entryType AS entryType,
        cl.amount AS amount,
        cl.balanceAfter AS balanceAfter,
        cl.createdAt AS createdAt,

        t.txnType AS txnType,
        t.txnCategory AS txnCategory,
        t.channel AS channel,
        t.initiatedBy AS initiatedBy,
        t.sourceUser.userId AS sourceUserId,
        t.targetUser.userId AS targetUserId,
        t.remarks AS remarks,

        su.fullName AS sourceName,
        su.mobileNo AS sourceMobile,
        tu.fullName AS targetName,
        tu.mobileNo AS targetMobile

    FROM CashLedger cl
    LEFT JOIN Transaction t ON cl.transaction.txnId = t.txnId
    LEFT JOIN t.sourceUser su
    LEFT JOIN t.targetUser tu

    WHERE cl.user.userId = :userId
""")
    List<TransactionProjection> fetchCashLedger(@Param("userId") String userId);

    @Query("""
        SELECT COALESCE(SUM(t.amount),0)
        FROM Transaction t
        WHERE t.txnType = :type
        AND t.sourceUser.userId = :userId
        AND DATE(t.createdAt) = CURRENT_DATE
    """)
    Double getTodayAmount(@Param("type") TransactionType type,
                          @Param("userId") String userId);

    @Query("""
        SELECT COALESCE(SUM(t.amount),0)
        FROM Transaction t
        WHERE t.sourceUser.userId = :userId
        AND t.txnType = 'COMMISSION'
        AND FUNCTION('DATE', t.createdAt) = CURRENT_DATE
    """)
    Double getTodayCommission(String userId);


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