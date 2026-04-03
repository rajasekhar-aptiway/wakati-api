package com.wakati.repository;

import com.wakati.entity.CustomerTransferLimits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerTransferLimitsRepository extends JpaRepository<CustomerTransferLimits, Long> {

    Optional<CustomerTransferLimits> findByCustomerId(String customerId);

    List<CustomerTransferLimits> findBySetBy(String setBy);
}
