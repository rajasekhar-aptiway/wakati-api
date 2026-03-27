package com.wakati.repository;

import com.wakati.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r FROM RefreshToken r
        WHERE r.tokenHash = :hash
    """)
    Optional<RefreshToken> findForUpdate(@Param("hash") String hash);

    @Modifying
    @Query("""
        UPDATE RefreshToken r
        SET r.revoked = true
        WHERE r.user.userId = :userId
    """)
    void revokeAllByUserId(@Param("userId") String userId);
}
