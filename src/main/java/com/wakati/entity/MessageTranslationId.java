package com.wakati.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MessageTranslationId implements Serializable {

    private String messageKey;
    private String language;

    public MessageTranslationId() {}

    public MessageTranslationId(String messageKey, String language) {
        this.messageKey = messageKey;
        this.language = language;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    // 🔥 VERY IMPORTANT (must implement)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageTranslationId that)) return false;
        return Objects.equals(messageKey, that.messageKey) &&
               Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageKey, language);
    }
}