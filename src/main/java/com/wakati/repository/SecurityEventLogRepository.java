package com.wakati.repository;

import com.wakati.entity.SecurityEventLog;
import com.wakati.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityEventLogRepository extends JpaRepository<SecurityEventLog,Long> {

    public List<SecurityEventLog> findByEventType(EventType eventType);

    public List<SecurityEventLog> findByUserId(String userId);

    public List<SecurityEventLog> findByUserIdAndEventType(String userId, EventType eventType);

    public List<SecurityEventLog> findByEventTypeAndCreatedAtBetween(EventType eventType, LocalDateTime start, LocalDateTime end);
}

