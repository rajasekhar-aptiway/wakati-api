package com.wakati.repository;

import com.wakati.entity.OtpChallenge;
import com.wakati.enums.Purpose;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpChallengeRepository extends JpaRepository<OtpChallenge,Long> {

    public Optional<OtpChallenge> findByOtpId(String otpId);

    public List<OtpChallenge> findByPurpose(Purpose purpose);

    public List<OtpChallenge> findByUserId(String userId);

    public List<OtpChallenge> findByUserIdAndPurpose(String userId, Purpose purpose);

    public List<OtpChallenge> findByUserIdAndPurposeAndVerifiedTrue(String userId, Purpose purpose);

    @Query("""
        SELECT o FROM OtpChallenge o
        WHERE o.userId = :userId
        ORDER BY o.createdAt DESC
    """)
    List<OtpChallenge> findLatestOtp(@Param("userId") String userId, Pageable pageable);
}
