package com.wakati.repository;

import com.wakati.entity.User;
import com.wakati.model.response.UserBasicProjection;
import com.wakati.model.response.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DealerRepository extends JpaRepository<User, Long> {

    // ✅ WEB DEALERS
    @Query(value = """
        SELECT 
            u.user_id AS userId,
            u.user_type AS userType,
            u.full_name AS fullName,
            u.mobile_no AS mobileNo,
            u.email AS email,
            u.status AS status,
            u.created_by AS createdBy,
            u.created_at AS createdAt,

            ua.attribute_id AS attributeId,
            ua.attribute_key AS attributeKey,
            ua.attribute_value AS attributeValue,

            ud.document_id AS documentId,
            ud.document_type AS documentType,
            ud.document_number AS documentNumber,
            ud.document_url AS documentUrl,
            ud.verification_status AS verificationStatus,

            w.wallet_id AS walletId

        FROM users u
        LEFT JOIN user_attributes ua ON u.user_id = ua.user_id
        LEFT JOIN user_documents ud ON u.user_id = ud.user_id
        LEFT JOIN wallet w ON w.owner_id = u.user_id

        WHERE u.user_type IN ('DEALER','SUPER_DEALER')
        AND (:status IS NULL OR u.status = :status)

        ORDER BY u.user_id DESC
    """, nativeQuery = true)
    List<UserProjection> fetchWebDealers(@Param("status") String status);

    // ✅ COUNTS
    @Query(value = "SELECT COUNT(*) FROM users WHERE user_type = 'DEALER' AND status = :status", nativeQuery = true)
    int countDealerByStatus(@Param("status") String status);

    // ✅ USER BASIC
    @Query(value = "SELECT user_type AS userType, island AS island FROM users WHERE user_id = :userId", nativeQuery = true)
    UserBasicProjection getUserBasic(@Param("userId") String userId);

    // ✅ RECEIVER
    @Query(value = """
        SELECT DISTINCT u.user_id, u.full_name, u.mobile_no, u.email, u.status, u.island
        FROM users u
        JOIN user_attributes ua ON ua.user_id = u.user_id
        WHERE u.user_type = 'DEALER'
        AND u.island = :island
        AND ua.attribute_key = 'SUB_CATEGORY'
        AND ua.attribute_value = 'HURIMONEY'
    """, nativeQuery = true)
    List<Map<String, Object>> fetchReceiverDealers(@Param("island") String island);

    // ✅ PARTNER_AGENT
    @Query(value = """
        SELECT DISTINCT u.user_id, u.full_name, u.mobile_no, u.email, u.status, u.island
        FROM users u
        JOIN user_attributes ua ON ua.user_id = u.user_id
        WHERE u.user_type = 'DEALER'
        AND u.island = :island
        AND ua.attribute_key = 'SUB_CATEGORY'
        AND ua.attribute_value = 'PARTNER_AGENT'
    """, nativeQuery = true)
    List<Map<String, Object>> fetchPartnerAgentDealers(@Param("island") String island);

    // ✅ SUPER DEALER
    @Query(value = """
        SELECT DISTINCT u.user_id, u.full_name, u.mobile_no, u.email, u.status
        FROM dealer_assignment da
        JOIN users u ON u.user_id = da.dealer_id
        JOIN user_attributes ua ON ua.user_id = u.user_id
        WHERE da.super_dealer_id = :userId
        AND ua.attribute_key = 'SUB_CATEGORY'
        AND ua.attribute_value = :subCategory
    """, nativeQuery = true)
    List<Map<String, Object>> fetchSuperDealerDealers(@Param("userId") String userId,
                                                      @Param("subCategory") String subCategory);

    // ✅ SUB CATEGORY
    @Query(value = "SELECT attribute_value FROM user_attributes WHERE user_id = :userId AND attribute_key = 'SUB_CATEGORY' LIMIT 1", nativeQuery = true)
    String getSubCategory(@Param("userId") String userId);

    @Query("""
    SELECT 
        u.status, COUNT(u)
    FROM User u
    GROUP BY u.status
""")
    List<Object[]> countByStatusGrouped();
}