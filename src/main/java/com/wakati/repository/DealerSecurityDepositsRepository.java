package com.wakati.repository;

import com.wakati.entity.DealerSecurityDeposits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerSecurityDepositsRepository extends JpaRepository<DealerSecurityDeposits, Long> {
    //Declared in Copy repository
    Optional<DealerSecurityDeposits> findByCode(String Code);
}
