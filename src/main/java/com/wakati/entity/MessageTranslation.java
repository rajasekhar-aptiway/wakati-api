package com.wakati.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "i18n_messages")
public class MessageTranslation {
    @EmbeddedId
    private MessageTranslationId id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    public MessageTranslation() {}

    public MessageTranslation(MessageTranslationId id, String message) {
        this.id = id;
        this.message = message;
    }

    public MessageTranslationId getId() {
        return id;
    }

    public void setId(MessageTranslationId id) {
        this.id = id;
    }

    public String getMessageKey() {
        return id != null ? id.getMessageKey() : null;
    }

    public String getLanguage() {
        return id != null ? id.getLanguage() : null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
