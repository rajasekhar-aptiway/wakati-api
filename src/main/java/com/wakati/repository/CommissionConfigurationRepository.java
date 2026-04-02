package com.wakati.repository;

import com.wakati.entity.CommissionConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionConfigurationRepository extends JpaRepository<CommissionConfiguration, Long> {

    Optional<CommissionConfiguration> findByCommissionId(String commissionId);

    List<CommissionConfiguration> findByTxnType(String txnType);

    List<CommissionConfiguration> findByChannel(String channel);

    List<CommissionConfiguration> findByStatus(String status);

    List<CommissionConfiguration> findByUpdatedBy(String updatedBy);

    Optional<CommissionConfiguration> findByTxnTypeAndChannelAndStatus(
            String transactionType,
            String channel,
            String status
    );
}
