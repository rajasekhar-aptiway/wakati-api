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



}