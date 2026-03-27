package com.wakati.repository;

import com.wakati.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    public List<RefreshToken> findByUserId(String userId);

    public Optional<RefreshToken> findByTokenHash(String tokenHash);

    public List<RefreshToken> findByUserIdAndRevokedFalse(String userId);

    public Optional<RefreshToken> findByTokenHashAndRevokedFalseAndExpiresAtAfter(String tokenHash, LocalDateTime now);
}
