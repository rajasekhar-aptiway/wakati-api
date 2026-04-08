package com.wakati.repository;

import com.wakati.entity.User;
import com.wakati.entity.Wallet;
import com.wakati.enums.AccountStatus;
import com.wakati.enums.OwnerType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Optional<Wallet> findByWalletId(String walletId);

    Optional<Wallet> findByOwnerId(String ownerId);

    Optional<Wallet> findByOwner(User owner);

    Optional<Wallet> findByOwner_UserId(String ownerId);

    List<Wallet> findByOwnerType(OwnerType ownerType);

    List<Wallet> findByStatus(AccountStatus status);

    @Modifying
    @Query("""
    UPDATE Wallet w
    SET w.status = :status
    WHERE w.owner.userId = :userId
""")
    void updateStatusByOwnerId(@Param("userId") String userId,
                               @Param("status") AccountStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.owner.userId = :ownerId")
    Optional<Wallet> findByOwnerIdForUpdate(@Param("ownerId") String ownerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.owner.userId = :ownerId")
    Wallet lockByOwnerId(@Param("ownerId") String ownerId);


//    @Query("SELECT COALESCE(w.balance,0) FROM Wallet w WHERE w.ownerId = :userId")
//    Double getWalletBalance(@Param("userId") String userId);
}