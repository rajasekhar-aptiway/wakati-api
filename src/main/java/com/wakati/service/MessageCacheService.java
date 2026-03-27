package com.wakati.service;

import com.wakati.entity.MessageTranslation;
import com.wakati.repository.MessageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageCacheService {

    @Autowired
    private MessageRepository repository;

    private final Map<String, Map<String, String>> messages = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadMessages() {
        List<MessageTranslation> list = repository.findAll();

        for (MessageTranslation mt : list) {
            String key = mt.getId().getMessageKey();
            String lang = mt.getId().getLanguage();

            messages
                .computeIfAbsent(key, k -> new ConcurrentHashMap<>())
                .put(lang, mt.getMessage());
        }

        System.out.println("✅ Loaded " + list.size() + " messages into cache");
    }

    public String getMessage(String key, String lang) {

        Map<String, String> langMap = messages.get(key);

        if (langMap == null) {
            return key; // fallback
        }

        // 1️⃣ Preferred language
        if (langMap.containsKey(lang)) {
            return langMap.get(lang);
        }

        // 2️⃣ Fallback to English
        if (langMap.containsKey("en")) {
            return langMap.get("en");
        }

        // 3️⃣ Any available
        return langMap.values().stream().findFirst().orElse(key);
    }
}