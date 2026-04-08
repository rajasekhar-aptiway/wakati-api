package com.wakati.repository;

import com.wakati.entity.CustomerClosureRequest;
import com.wakati.entity.User;
import com.wakati.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerClosureRequestRepository extends JpaRepository<CustomerClosureRequest, String> {

    Optional<CustomerClosureRequest> findByClosureRequestId(String closureRequestId);

    Optional<CustomerClosureRequest> findByCustomerId(String customerId);

    List<CustomerClosureRequest> findByRequestedBy(String requestedBy);

    List<CustomerClosureRequest> findByRequestedByType(String requestedByType);

    List<CustomerClosureRequest> findByStatus(String status);

    List<CustomerClosureRequest> findByApprovedBy(String approvedBy);

    boolean existsByCustomerAndStatus(User customer, Status status);
}
