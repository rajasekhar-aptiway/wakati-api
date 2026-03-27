package com.wakati.repository;

import com.wakati.entity.AdminActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminActionLogRepository extends JpaRepository<AdminActionLog, Long> {

    Optional<AdminActionLog> findById(Long aLong);

    Optional<AdminActionLog> findByTargetUserId(String targetUserId);

    List<AdminActionLog> findByActionType(String actionType);
}
