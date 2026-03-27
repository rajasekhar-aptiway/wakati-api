package com.wakati.repository;

import com.wakati.entity.Cash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashRepository extends JpaRepository<Cash, Long> {

    Optional<Cash> findByUser(String userId);

    @Query("SELECT COALESCE(c.balance,0) FROM Cash c WHERE c.user.userId = :userId")
    Double getCashBalance(@Param("userId") String userId);
}
