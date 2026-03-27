package com.wakati.repository;

import com.wakati.entity.PlatformAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformAccountRepository extends JpaRepository<PlatformAccount, Long> {

    public Optional<PlatformAccount> findByAccountCode(String accountCode);

    public Optional<PlatformAccount> findByPlatformIdAndAccountCode(String platformId, String accountCode);

    public List<PlatformAccount> findByPlatformId(Long platformId);

    public List<PlatformAccount> findByPlatformIdAndStatus(
            String platformId, String status);

    public List<PlatformAccount> findByStatus(String status);
}
