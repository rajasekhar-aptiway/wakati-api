package com.wakati.repository;

import com.wakati.entity.TransactionDispute;
import com.wakati.enums.DisputeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransactionDisputeRepository extends JpaRepository<TransactionDispute, String> {

    List<TransactionDispute> findByTransaction(String txnId);

    List<TransactionDispute> findByRaisedBy(String userId);

    List<TransactionDispute> findByStatus(DisputeStatus status);

    /* VALIDATE TXN */
    @Query(value = """
        SELECT COUNT(*) > 0
        FROM TRANSACTIONS
        WHERE txn_id = :txnId
        AND (source_user_id = :userId OR target_user_id = :userId)
    """, nativeQuery = true)
    boolean validateTransactionOwnership(String txnId, String userId);


    /* DUPLICATE */
    @Query(value = """
        SELECT COUNT(*) > 0
        FROM TRANSACTION_DISPUTES
        WHERE txn_id = :txnId
        AND raised_by = :userId
        AND status IN ('OPEN','UNDER_REVIEW')
    """, nativeQuery = true)
    boolean existsOpenDispute(String txnId, String userId);


    /* INSERT */
    @Modifying
    @Query(value = """
        INSERT INTO TRANSACTION_DISPUTES
        (dispute_id, txn_id, raised_by, reason, status, created_at)
        VALUES (:id, :txnId, :userId, :reason, 'OPEN', NOW())
    """, nativeQuery = true)
    void insertDispute(String id, String txnId, String userId, String reason);


    /* COUNT */
    @Query(value = """
        SELECT COUNT(*)
        FROM TRANSACTION_DISPUTES d
        WHERE (:isAdmin = true OR d.raised_by = :userId)
        AND (:status = '' OR d.status = :status)
    """, nativeQuery = true)
    long countDisputes(String userId, String status, boolean isAdmin);


    /* FETCH */
    @Query(value = """
        SELECT d.*, t.txn_type, t.amount, u.full_name
        FROM TRANSACTION_DISPUTES d
        JOIN TRANSACTIONS t ON t.txn_id = d.txn_id
        LEFT JOIN USERS u ON u.user_id = d.raised_by
        WHERE (:isAdmin = true OR d.raised_by = :userId)
        AND (:status = '' OR d.status = :status)
        ORDER BY d.created_at DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Map<String, Object>> fetchDisputes(
            String userId, String status, boolean isAdmin,
            int limit, int offset
    );


    /* STATUS */
    @Query(value = "SELECT status FROM TRANSACTION_DISPUTES WHERE dispute_id = :id", nativeQuery = true)
    String getDisputeStatus(String id);


    /* UPDATE */
    @Modifying
    @Query(value = """
        UPDATE TRANSACTION_DISPUTES
        SET status = :status,
            resolved_by = :userId,
            resolution = :resolution,
            updated_at = NOW()
        WHERE dispute_id = :id
    """, nativeQuery = true)
    void updateDispute(String status, String userId, String resolution, String id);


    /* ADMIN LOG */
    @Modifying
    @Query(value = """
        INSERT INTO ADMIN_ACTION_LOG
        (performed_by, action_type, target_user_id, details, ip_address, created_at)
        VALUES (:userId, 'DISPUTE_UPDATE', NULL,
                JSON_OBJECT('dispute_id', :disputeId, 'new_status', :status, 'resolution', :resolution),
                :ip, NOW())
    """, nativeQuery = true)
    void insertAdminLog(String userId, String disputeId, String status, String resolution, String ip);
}