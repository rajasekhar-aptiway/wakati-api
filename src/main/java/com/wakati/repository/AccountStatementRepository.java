package com.wakati.repository;

import com.wakati.entity.Transaction;
import com.wakati.model.response.StatementProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountStatementRepository extends JpaRepository<Transaction,Integer> {

    @Query(value = """
    SELECT *
    FROM (
        SELECT
            wl.txn_id,
            wl.entry_type AS direction,
            wl.amount,
            wl.balance_after,
            wl.created_at,
            t.txn_type,
            t.source_user_id,
            t.target_user_id,
            su.full_name AS source_name,
            su.mobile_no AS source_mobile,
            tu.full_name AS target_name,
            tu.mobile_no AS target_mobile,
            COALESCE(comm.amount, 0) AS commission_amount,
            CASE 
                WHEN t.txn_type = 'CASH_WITHDRAWAL' AND wl.entry_type = 'DEBIT' THEN 1
                ELSE 0
            END AS commission_in_amount
        FROM WALLET_LEDGER wl
        JOIN TRANSACTIONS t ON t.txn_id = wl.txn_id
        LEFT JOIN TRANSACTIONS comm 
            ON comm.parent_txn_id = t.txn_id AND comm.txn_type = 'COMMISSION'
        LEFT JOIN USERS su ON su.user_id = t.source_user_id
        LEFT JOIN USERS tu ON tu.user_id = t.target_user_id
        WHERE wl.user_id = :userId
          AND t.txn_type <> 'COMMISSION'
          AND (:fromDate IS NULL OR DATE(wl.created_at) >= :fromDate)
          AND (:toDate IS NULL OR DATE(wl.created_at) <= :toDate)

        UNION ALL

        SELECT
            t.txn_id,
            'CREDIT',
            t.amount,
            NULL,
            t.created_at,
            t.txn_type,
            t.source_user_id,
            t.target_user_id,
            su.full_name,
            su.mobile_no,
            tu.full_name,
            tu.mobile_no,
            0,
            0
        FROM TRANSACTIONS t
        LEFT JOIN USERS su ON su.user_id = t.source_user_id
        LEFT JOIN USERS tu ON tu.user_id = t.target_user_id
        WHERE t.txn_type = 'COMMISSION'
          AND t.source_user_id = :userId
    ) combined
    ORDER BY created_at DESC
    LIMIT :limit OFFSET :offset
""", nativeQuery = true)
    List<StatementProjection> fetchOptimizedStatement(
            String userId,
            LocalDate fromDate,
            LocalDate toDate,
            int limit,
            int offset
    );

    @Query(value = """
    SELECT COUNT(*) FROM (
        SELECT wl.txn_id
        FROM WALLET_LEDGER wl
        JOIN TRANSACTIONS t ON t.txn_id = wl.txn_id
        WHERE wl.user_id = :userId
          AND t.txn_type <> 'COMMISSION'

        UNION ALL

        SELECT t.txn_id
        FROM TRANSACTIONS t
        WHERE t.txn_type = 'COMMISSION'
          AND t.source_user_id = :userId
    ) c
""", nativeQuery = true)
    long countStatement(String userId);


}
