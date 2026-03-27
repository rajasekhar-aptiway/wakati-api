package com.wakati.repository;

import com.wakati.entity.Wallet;
import com.wakati.enums.AccountStatus;
import com.wakati.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Optional<Wallet> findByWalletId(String walletId);

    Optional<Wallet> findByOwnerId(String ownerId);

    List<Wallet> findByOwnerType(OwnerType ownerType);

    List<Wallet> findByStatus(AccountStatus status);

//    @Query("SELECT COALESCE(w.balance,0) FROM Wallet w WHERE w.ownerId = :userId")
//    Double getWalletBalance(@Param("userId") String userId);
}