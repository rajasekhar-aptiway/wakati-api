package com.wakati.repository;

import com.wakati.entity.EmailTemplate;
import com.wakati.entity.EmailTemplateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplatesRepository extends JpaRepository<EmailTemplate, EmailTemplateId> {

    public Optional<EmailTemplate> findByTemplateKeyAndLanguage(
            String templateKey, String language);

    public List<EmailTemplate> findByTemplateKey(String templateKey);

    public List<EmailTemplate> findByLanguage(String language);

    public List<EmailTemplate> findByIsActiveTrue();
}

