package com.wakati.repository;

import com.wakati.entity.SmsTemplate;
import com.wakati.entity.SmsTemplateId;
import com.wakati.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, SmsTemplateId> {

    Optional<SmsTemplate> findByTemplateKeyAndLanguage(String templateKey, Language language);

    List<SmsTemplate> findByTemplateKey(String templateKey);

    List<SmsTemplate> findByIsActiveTrue();


    List<SmsTemplate> findByTemplateKeyOrderByLanguageAsc(String key);

    List<SmsTemplate> findByLanguageOrderByTemplateKeyAsc(Language language);

    List<SmsTemplate> findAllByOrderByTemplateKeyAscLanguageAsc();
}