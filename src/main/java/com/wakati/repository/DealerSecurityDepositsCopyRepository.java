package com.wakati.repository;

import com.wakati.entity.DealerSecurityDepositsCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealerSecurityDepositsCopyRepository extends JpaRepository<DealerSecurityDepositsCopy, Long> {

    Optional<DealerSecurityDepositsCopy> findByDealerId(String dealerId);

    Optional<DealerSecurityDepositsCopy> findByCode(String code);

    List<DealerSecurityDepositsCopy> findByDisconnecton(String disconnecton);
}
