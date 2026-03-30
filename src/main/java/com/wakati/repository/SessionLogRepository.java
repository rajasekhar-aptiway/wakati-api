package com.wakati.repository;

import com.wakati.entity.SessionLog;
import com.wakati.enums.LogoutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, Long> {

    public Optional<SessionLog> findBySessionToken(String sessionToken);

    public Optional<SessionLog> findBySessionTokenAndLogoutAtIsNull(String sessionToken);

    public List<SessionLog> findByUserId(String userId);

    public List<SessionLog> findByLogoutType(LogoutType logoutType);

    public List<SessionLog> findByUserIdAndLogoutAtIsNull(String userId);

    public List<SessionLog> findByUserIdAndLoginAtBetween(String userId, LocalDateTime start, LocalDateTime end);

    public List<SessionLog> findByLogoutAtIsNullAndLoginAtBefore(LocalDateTime time);

    @Modifying
    @Query("""
    UPDATE SessionLog s
    SET s.logoutAt = CURRENT_TIMESTAMP,
        s.logoutType = 'FORCED'
    WHERE s.userId = :userId AND s.logoutAt IS NULL
""")
    void closeActiveSessions(@Param("userId") String userId);
}