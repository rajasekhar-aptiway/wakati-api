package com.wakati.repository;

import com.wakati.entity.CustomerTransferLimits;
import com.wakati.entity.User;
import com.wakati.model.response.CustomerTransferLimitProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerTransferLimitsRepository extends JpaRepository<CustomerTransferLimits, Long> {

    Optional<CustomerTransferLimits> findByCustomerId(String customerId);
    List<CustomerTransferLimits> findBySetBy(String setBy);


    Optional<CustomerTransferLimits> findByCustomer(User customer);

    Optional<CustomerTransferLimits> findByCustomerIsNull();

    @Query("""
        SELECT 
            c.customer.userId AS customerUserId,
            c.customer.fullName AS customerName,
            c.dailyTransferLimit AS dailyTransferLimit,
            c.singleTransferLimit AS singleTransferLimit,
            c.setBy.userId AS setByUserId
        FROM CustomerTransferLimits c
        WHERE (:userId IS NULL AND c.customer IS NULL)
           OR (c.customer.userId = :userId)
        ORDER BY c.customer DESC
        """)
    Optional<CustomerTransferLimitProjection> findLimitProjection(@Param("userId") String userId);
}
