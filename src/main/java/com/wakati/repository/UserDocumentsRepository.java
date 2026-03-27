package com.wakati.repository;

import com.wakati.entity.UserDocuments;
import com.wakati.enums.DocumentType;
import com.wakati.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocumentsRepository extends JpaRepository<UserDocuments, Integer> {

    List<UserDocuments> findByUserId(String userId);

    List<UserDocuments> findByUserIdAndDocumentType(String userId, DocumentType documentType);

    List<UserDocuments> findByVerificationStatus(VerificationStatus status);
}