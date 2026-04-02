package com.wakati.repository;

import com.wakati.entity.User;
import com.wakati.model.request.AuditReportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuditReportRepository extends JpaRepository<User,Integer> {

    @Query(value = """
        SELECT u.user_type AS role, COUNT(*) AS total,
        SUM(CASE WHEN u.status = 'APPROVED' THEN 1 ELSE 0 END) AS approved,
        SUM(CASE WHEN u.status = 'PENDING' THEN 1 ELSE 0 END) AS pending,
        SUM(CASE WHEN u.status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected
        FROM USERS u
        WHERE (:userType IS NULL OR u.user_type = :userType)
        GROUP BY u.user_type
    """, nativeQuery = true)
    List<Map<String, Object>> userCreationSummary(AuditReportRequest req);

    @Query(value = """
        SELECT COUNT(*) 
        FROM USERS u
        WHERE (:userType IS NULL OR u.user_type = :userType)
        AND (:status IS NULL OR u.status = :status)
        AND (:userId IS NULL OR u.created_by = :userId)
        AND (:fromDate IS NULL OR DATE(u.created_at) >= :fromDate)
        AND (:toDate IS NULL OR DATE(u.created_at) <= :toDate)
    """, nativeQuery = true)
    long userCreationCount(
            @Param("userType") String userType,
            @Param("status") String status,
            @Param("userId") String userId,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate
    );


    @Query(value = """
        SELECT u.user_id, u.user_type, u.full_name, u.mobile_no, u.email,
               u.island, u.region, u.status, u.created_by, u.created_at,
               u.registration_stage, u.verified_by_admin, u.verified_by_adjudicator,
               c.full_name AS created_by_name
        FROM USERS u
        LEFT JOIN USERS c ON c.user_id = u.created_by
        WHERE (:userType IS NULL OR u.user_type = :userType)
        AND (:status IS NULL OR u.status = :status)
        AND (:userId IS NULL OR u.created_by = :userId)
        AND (:fromDate IS NULL OR DATE(u.created_at) >= :fromDate)
        AND (:toDate IS NULL OR DATE(u.created_at) <= :toDate)
        ORDER BY u.created_at DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Map<String, Object>> userCreationRecords(
            @Param("userType") String userType,
            @Param("status") String status,
            @Param("userId") String userId,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    /* =========================================================
       USER TYPE FETCH (for RBAC)
       ========================================================= */

    @Query(value = "SELECT user_type FROM USERS WHERE user_id = :userId LIMIT 1", nativeQuery = true)
    String getUserType(@Param("userId") String userId);


    /* =========================================================
       WALLET TO WALLET
       ========================================================= */

    @Query(value = """
        SELECT COUNT(*)
        FROM TRANSACTIONS t
        WHERE t.txn_type = 'TRANSFER'
        AND (:channel IS NULL OR t.channel = :channel)
        AND (:userId IS NULL OR (t.source_user_id = :userId OR t.target_user_id = :userId))
        AND (:fromDate IS NULL OR DATE(t.created_at) >= :fromDate)
        AND (:toDate IS NULL OR DATE(t.created_at) <= :toDate)
    """, nativeQuery = true)
    long walletTxnCount(
            @Param("userId") String userId,
            @Param("channel") String channel,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate
    );


    @Query(value = """
        SELECT t.txn_id, t.channel, t.initiated_by,
               t.source_user_id, t.target_user_id,
               t.amount, t.remarks, t.status, t.created_at,
               su.full_name AS source_name,
               tu.full_name AS target_name
        FROM TRANSACTIONS t
        LEFT JOIN USERS su ON su.user_id = t.source_user_id
        LEFT JOIN USERS tu ON tu.user_id = t.target_user_id
        WHERE t.txn_type = 'TRANSFER'
        AND (:channel IS NULL OR t.channel = :channel)
        AND (:userId IS NULL OR (t.source_user_id = :userId OR t.target_user_id = :userId))
        AND (:fromDate IS NULL OR DATE(t.created_at) >= :fromDate)
        AND (:toDate IS NULL OR DATE(t.created_at) <= :toDate)
        ORDER BY t.created_at DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Map<String, Object>> walletTxnRecords(
            @Param("userId") String userId,
            @Param("channel") String channel,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    /* =========================================================
       COMMISSIONS
       ========================================================= */

    @Query(value = """
        SELECT COUNT(*) AS total_receipts,
               COALESCE(SUM(wl.amount),0) AS total_received
        FROM WALLET_LEDGER wl
        JOIN TRANSACTIONS t ON t.txn_id = wl.txn_id
        WHERE wl.entry_type = 'CREDIT'
        AND t.txn_type = 'COMMISSION'
        AND (:userId IS NULL OR wl.user_id = :userId)
        AND (:fromDate IS NULL OR DATE(wl.created_at) >= :fromDate)
        AND (:toDate IS NULL OR DATE(wl.created_at) <= :toDate)
    """, nativeQuery = true)
    Map<String, Object> commissionAggregate(
            @Param("userId") String userId,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate
    );


    @Query(value = """
        SELECT wl.user_id AS recipient_id,
               u.full_name AS recipient_name,
               u.user_type AS recipient_type,
               COUNT(*) AS receipt_count,
               COALESCE(SUM(wl.amount),0) AS total_received
        FROM WALLET_LEDGER wl
        JOIN TRANSACTIONS t ON t.txn_id = wl.txn_id
        LEFT JOIN USERS u ON u.user_id = wl.user_id
        WHERE wl.entry_type = 'CREDIT'
        AND t.txn_type = 'COMMISSION'
        AND (:userId IS NULL OR wl.user_id = :userId)
        AND (:fromDate IS NULL OR DATE(wl.created_at) >= :fromDate)
        AND (:toDate IS NULL OR DATE(wl.created_at) <= :toDate)
        GROUP BY wl.user_id, u.full_name, u.user_type
        ORDER BY total_received DESC
    """, nativeQuery = true)
    List<Map<String, Object>> commissionSummary(
            @Param("userId") String userId,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate
    );


    @Query(value = """
        SELECT t.txn_id,
               wl.user_id AS recipient_id,
               wl.amount AS amount_received,
               wl.created_at,
               u.full_name,
               u.mobile_no,
               u.user_type
        FROM WALLET_LEDGER wl
        JOIN TRANSACTIONS t ON t.txn_id = wl.txn_id
        LEFT JOIN USERS u ON u.user_id = wl.user_id
        WHERE wl.entry_type = 'CREDIT'
        AND t.txn_type = 'COMMISSION'
        AND (:userId IS NULL OR wl.user_id = :userId)
        AND (:fromDate IS NULL OR DATE(wl.created_at) >= :fromDate)
        AND (:toDate IS NULL OR DATE(wl.created_at) <= :toDate)
        ORDER BY wl.created_at DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Map<String, Object>> commissionRecords(
            @Param("userId") String userId,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(value = """
    SELECT user_type, COUNT(*) AS total
    FROM USERS
    WHERE (:fromDate IS NULL OR DATE(created_at) >= :fromDate)
    AND (:toDate IS NULL OR DATE(created_at) <= :toDate)
    GROUP BY user_type
""", nativeQuery = true)
    List<Map<String, Object>> usersByRoleSummary(
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate
    );

    @Query(value = """
    SELECT status, COUNT(*) AS total
    FROM USERS
    WHERE (:fromDate IS NULL OR DATE(created_at) >= :fromDate)
    AND (:toDate IS NULL OR DATE(created_at) <= :toDate)
    GROUP BY status
""", nativeQuery = true)
    List<Map<String, Object>> usersByStatusSummary(
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate
    );

    @Query(value = """
    SELECT COUNT(*)
    FROM TRANSACTIONS
    WHERE txn_type = 'WITHDRAW'
    AND (:userId IS NULL OR source_user_id = :userId)
    AND (:fromDate IS NULL OR DATE(created_at) >= :fromDate)
    AND (:toDate IS NULL OR DATE(created_at) <= :toDate)
""", nativeQuery = true)
    long withdrawalCount(String userId, String fromDate, String toDate);

    @Query(value = """
    SELECT txn_id, source_user_id, amount, status, created_at
    FROM TRANSACTIONS
    WHERE txn_type = 'WITHDRAW'
    AND (:userId IS NULL OR source_user_id = :userId)
    AND (:fromDate IS NULL OR DATE(created_at) >= :fromDate)
    AND (:toDate IS NULL OR DATE(created_at) <= :toDate)
    ORDER BY created_at DESC
    LIMIT :limit OFFSET :offset
""", nativeQuery = true)
    List<Map<String, Object>> withdrawalRecords(
            String userId,
            String fromDate,
            String toDate,
            int limit,
            int offset
    );

    @Query(value = """
    SELECT COUNT(*)
    FROM TRANSACTIONS
    WHERE txn_type = 'DEPOSIT'
    AND (:userId IS NULL OR target_user_id = :userId)
    AND (:fromDate IS NULL OR DATE(created_at) >= :fromDate)
    AND (:toDate IS NULL OR DATE(created_at) <= :toDate)
""", nativeQuery = true)
    long depositCount(String userId, String fromDate, String toDate);

    @Query(value = """
    SELECT txn_id, target_user_id, amount, status, created_at
    FROM TRANSACTIONS
    WHERE txn_type = 'DEPOSIT'
    AND (:userId IS NULL OR target_user_id = :userId)
    AND (:fromDate IS NULL OR DATE(created_at) >= :fromDate)
    AND (:toDate IS NULL OR DATE(created_at) <= :toDate)
    ORDER BY created_at DESC
    LIMIT :limit OFFSET :offset
""", nativeQuery = true)
    List<Map<String, Object>> depositRecords(
            String userId,
            String fromDate,
            String toDate,
            int limit,
            int offset
    );

    @Query(value = """
    SELECT
        (SELECT COUNT(*) FROM USERS) AS total_users,
        (SELECT COUNT(*) FROM TRANSACTIONS) AS total_transactions,
        (SELECT COALESCE(SUM(amount),0) FROM TRANSACTIONS WHERE txn_type='DEPOSIT') AS total_deposits,
        (SELECT COALESCE(SUM(amount),0) FROM TRANSACTIONS WHERE txn_type='WITHDRAW') AS total_withdrawals
""", nativeQuery = true)
    Map<String, Object> platformOverview(String fromDate, String toDate);
}