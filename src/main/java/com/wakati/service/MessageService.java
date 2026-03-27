package com.wakati.service;

import com.wakati.I18NConstants;
import com.wakati.config.UserContext;
import com.wakati.entity.MessageTranslation;
import com.wakati.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class MessageService {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private MessageCacheService cacheService;

    @Autowired
    private UserContext userContext;

    public String getMessage(String key, String lang) {

        return repository.findByMessageKeyAndLanguage(key, lang)
                .map(MessageTranslation::getMessage)
                .orElseGet(() ->
                        repository.findByMessageKeyAndLanguage(key, "en")
                                .map(MessageTranslation::getMessage)
                                .orElse(key)
                );
    }


    private String get(String key, Object... args) {

        String lang = userContext.getCurrentLanguage();
        String message = cacheService.getMessage(key, lang);

        // Optional: parameter replacement
        if (args != null && args.length > 0) {
            return MessageFormat.format(message, args);
        }

        return message;
    }

    public String get(I18NConstants key, Object... args) {
        return get(key.name(),args);
    }
}