package com.wakati.repository;

import com.wakati.entity.Cash;
import com.wakati.model.response.TransactionProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashRepository extends JpaRepository<Cash, Long> {

    Optional<Cash> findByUser(String userId);

    @Query("SELECT COALESCE(c.balance,0) FROM Cash c WHERE c.user.userId = :userId")
    Double getCashBalance(@Param("userId") String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cash c WHERE c.user.userId = :userId")
    Cash lockByUserId(@Param("userId") String userId);



}
