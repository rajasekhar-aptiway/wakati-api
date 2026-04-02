package com.wakati.repository;

import com.wakati.entity.User;
import com.wakati.enums.RegistrationStage;
import com.wakati.enums.Status;
import com.wakati.enums.UserStatus;
import com.wakati.enums.UserType;
import com.wakati.model.response.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserId(String userId);

    Optional<User> findByMobileNo(String mobileNo);

    Optional<User> findByEmail(String email);

    List<User> findByStatus(UserStatus status);

    List<User> findByUserType(UserType userType);

    List<User> findByRegistrationStage(RegistrationStage stage);

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
            u.registration_stage AS registrationStage,
            u.verified_by_admin AS verifiedByAdmin,
            u.verified_by_adjudicator AS verifiedByAdjudicator,

            ua.attribute_id AS attributeId,
            ua.attribute_key AS attributeKey,
            ua.attribute_value AS attributeValue,

            ud.document_id AS documentId,
            ud.document_type AS documentType,
            ud.document_number AS documentNumber,
            ud.document_url AS documentUrl,
            ud.verification_status AS verificationStatus,

            w.wallet_id AS walletId

        FROM USERS u
        LEFT JOIN USER_ATTRIBUTES ua ON u.user_id = ua.user_id
        LEFT JOIN USER_DOCUMENTS ud ON u.user_id = ud.user_id
        LEFT JOIN WALLET w ON u.user_id = w.owner_id

        WHERE u.mobile_no = :mobileNo
        AND u.status IN ('APPROVED', 'CLOSED')
    """, nativeQuery = true)
    List<UserProjection> findUserByMobileNo(@Param("mobileNo") String mobileNo);

        @Query(value = """
        SELECT
            u.user_id AS userId,
            u.user_type AS userType,
            u.full_name AS fullName,
            u.mobile_no AS mobileNo,
            u.email AS email,
            u.island AS island,
            u.region AS region,
            u.status AS status,
            u.created_by AS createdBy,
            u.created_at AS createdAt,
            u.registration_stage AS registrationStage,

            ua.attribute_id AS attributeId,
            ua.attribute_key AS attributeKey,
            ua.attribute_value AS attributeValue

        FROM USERS u
        LEFT JOIN USER_ATTRIBUTES ua ON u.user_id = ua.user_id
        WHERE u.mobile_no = :mobileNo
    """, nativeQuery = true)
        List<UserProjection> findProfileStatusByMobile(@Param("mobileNo") String mobileNo);


    @Query("""
                SELECT 
                    u.userId AS userId,
                    u.userType AS userType,
                    u.fullName AS fullName,
                    u.mobileNo AS mobileNo,
                    u.email AS email,
                    u.status AS status,
                    u.createdBy AS createdBy,
                    u.createdAt AS createdAt,
                    u.registrationStage AS registrationStage,
                    u.verifiedByAdmin AS verifiedByAdmin,
                    u.verifiedByAdjudicator AS verifiedByAdjudicator,
            
                    ua.attributeId AS attributeId,
                    ua.attributeKey AS attributeKey,
                    ua.attributeValue AS attributeValue,
            
                    ud.documentId AS documentId,
                    ud.documentType AS documentType,
                    ud.documentNumber AS documentNumber,
                    ud.documentUrl AS documentUrl,
                    ud.verificationStatus AS verificationStatus,
            
                    w.walletId AS walletId
            
                FROM User u
                LEFT JOIN u.attributes ua
                LEFT JOIN u.documents ud
                LEFT JOIN u.wallets w
            
                WHERE u.userType IN :userTypes
                AND (:status IS NULL OR u.status = :status)
            
                ORDER BY u.userId DESC
               
            """)
    List<UserProjection> fetchUsers(
            @Param("status") Status status,
            @Param("userTypes") List<UserType> userTypes
    );

    @Query("SELECT u.userType FROM User u WHERE u.userId = :userId")
    String findUserTypeByUserId(Long userId);

    @Query("""
    SELECT 
        u.status, COUNT(u)
    FROM User u
    GROUP BY u.status
""")
    List<Object[]> countByStatusGrouped();




    @Query("""
    SELECT u.userId
    FROM User u
    ORDER BY u.createdAt DESC
    LIMIT :size OFFSET :offset
""")
    List<String> findUserIds(@Param("size") int size,
                             @Param("offset") int offset);

    @Query("""
    SELECT 
        u.userId AS userId,
        u.userType AS userType, 
        u.fullName AS fullName,
        u.mobileNo AS mobileNo,
        u.email AS email,
        u.status AS status,
        u.createdBy AS createdBy,
        u.createdAt AS createdAt,
        u.registrationStage AS registrationStage,
        u.verifiedByAdmin AS verifiedByAdmin,
        u.verifiedByAdjudicator AS verifiedByAdjudicator,

        ua.attributeId AS attributeId,
        ua.attributeKey AS attributeKey,
        ua.attributeValue AS attributeValue,

        ud.documentId AS documentId,
        ud.documentType AS documentType,
        ud.documentNumber AS documentNumber,
        ud.documentUrl AS documentUrl,
        ud.verificationStatus AS verificationStatus,

        w.walletId AS walletId

    FROM User u
    LEFT JOIN u.attributes ua
    LEFT JOIN u.documents ud
    LEFT JOIN u.wallets w

    WHERE u.userId IN :ids
    ORDER BY u.createdAt DESC
""")
    List<UserProjection> fetchUsersByIds(@Param("ids") List<String> ids);

    Optional<User> findTopByMobileNoAndStatusNotOrderByCreatedAtDesc(String mobileNo, Status status);

    Optional<User> findTopByEmailAndStatusNotOrderByCreatedAtDesc(String email, Status status);

    @Query("""
        SELECT u FROM User u
        WHERE u.mobileNo = :mobile
        ORDER BY 
            CASE WHEN u.status = 'APPROVED' THEN 0 ELSE 1 END,
            u.createdAt DESC
    """)
    List<User> findUsersByMobileOrdered(@Param("mobile") String mobile);



    @Query(value = """
    SELECT
        u.user_id AS userId,
        u.user_type AS userType,
        u.full_name AS fullName,
        u.mobile_no AS mobileNo,
        u.email AS email,
        u.island AS island,
        u.region AS region,
        u.status AS status,
        u.created_by AS createdBy,
        u.created_at AS createdAt,
        u.registration_stage AS registrationStage,
        u.verified_by_admin AS verifiedByAdmin,
        u.verified_by_adjudicator AS verifiedByAdjudicator,

        ua.attribute_id AS attributeId,
        ua.attribute_key AS attributeKey,
        ua.attribute_value AS attributeValue,

        ud.document_id AS documentId,
        ud.document_type AS documentType,
        ud.document_number AS documentNumber,
        ud.document_url AS documentUrl,
        ud.verification_status AS verificationStatus

    FROM USERS u
    LEFT JOIN USER_ATTRIBUTES ua ON u.user_id = ua.user_id
    LEFT JOIN USER_DOCUMENTS ud ON u.user_id = ud.user_id
    WHERE u.user_id = :userId
""", nativeQuery = true)
    List<UserProjection> findDashboardData(@Param("userId") String userId);

}