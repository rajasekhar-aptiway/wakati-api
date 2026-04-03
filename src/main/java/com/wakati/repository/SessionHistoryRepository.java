package com.wakati.repository;

import com.wakati.entity.SessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SessionHistoryRepository extends JpaRepository<SessionLog,Integer> {

    @Query(value = """
        SELECT COUNT(*) 
        FROM SESSION_LOG 
        WHERE user_id = :userId
    """, nativeQuery = true)
    long countSessions(@Param("userId") String userId);


    @Query(value = """
        SELECT session_token,
               ip_address,
               user_agent,
               login_at,
               logout_at,
               logout_type,
               duration_seconds
        FROM SESSION_LOG
        WHERE user_id = :userId
        ORDER BY login_at DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Map<String, Object>> fetchSessions(
            @Param("userId") String userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}