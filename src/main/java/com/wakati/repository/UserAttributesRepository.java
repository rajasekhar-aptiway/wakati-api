package com.wakati.repository;

import com.wakati.entity.UserAttributes;
import com.wakati.enums.UserType;
import com.wakati.model.response.UserWithExpiryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAttributesRepository extends JpaRepository<UserAttributes, Integer> {

    List<UserAttributes> findByUserId(String userId);

    Optional<UserAttributes> findByUserIdAndAttributeKey(String userId, String attributeKey);

    List<UserAttributes> findByAttributeKey(String attributeKey);

    @Query("""
        SELECT COUNT(ua)
        FROM UserAttributes ua
        JOIN User u ON ua.user.userId = u.userId
        WHERE ua.attributeKey = 'Idnumber'
        AND ua.attributeValue = :idNumber
        AND u.status <> 'REJECTED'
    """)
    long countByIdNumber(@Param("idNumber") String idNumber);

    @Query("""
    SELECT
        ua.user.userId AS userId,
        ua.user.userType AS userType,
        ua.user.fullName As fullName,
        ua.attributeValue AS idExpiry

    FROM UserAttributes ua

    WHERE ua.attributeKey = 'Idexpiry'
    AND ua.user.userType = UserType.CUSTOMER

    ORDER BY ua.createdAt DESC
""")
    List<UserWithExpiryProjection> findAllCustomersWithIdExpiry();
}