package com.wakati.repository;

import com.wakati.entity.UserDocuments;
import com.wakati.enums.DocumentType;
import com.wakati.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDocumentsRepository extends JpaRepository<UserDocuments, Integer> {

    List<UserDocuments> findByUserId(String userId);

    List<UserDocuments> findByUserIdAndDocumentType(String userId, DocumentType documentType);

    List<UserDocuments> findByVerificationStatus(VerificationStatus status);

    @Modifying
    @Query("""
    UPDATE UserDocuments d
    SET d.verificationStatus = :status
    WHERE d.user.userId = :userId
""")
    void updateStatusByUserId(@Param("userId") String userId,
                              @Param("status") VerificationStatus status);

    @Modifying
    @Query("""
        UPDATE UserDocuments d
        SET d.verificationStatus = 'EXPIRED'
        WHERE d.user.userId = :userId
        AND d.verificationStatus IN ('PENDING','REJECTED','APPROVED')
    """)
    void expireDocuments(@Param("userId") String userId);

    @Query("""
        SELECT d FROM UserDocuments d
        WHERE d.documentId = :documentId
        AND d.verificationStatus <> 'EXPIRED'
    """)
    Optional<UserDocuments> findByDocumentIdAndNotExpired(@Param("documentId") String documentId);
}