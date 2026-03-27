package com.wakati.repository;

import com.wakati.entity.MessageTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageTranslation, String> {

    Optional<MessageTranslation> findByMessageKeyAndLanguage(String messageKey, String language);
}