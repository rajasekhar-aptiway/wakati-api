package com.wakati.repository;

import com.wakati.entity.DealerTypeFinancialConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerTypeFinancialConfigRepository extends JpaRepository<DealerTypeFinancialConfig, Long> {

    Optional<DealerTypeFinancialConfig> findById(String id);

    Optional<DealerTypeFinancialConfig> findByDealerTypeId(String dealerTypeId);

}
