package com.wakati.notification;

import com.wakati.enums.Language;
import com.wakati.enums.NotificationType;

public class EmailRequest {
    private String templateId;
    private Language language;
    private NotificationType type;
    private String email;
    private String from;
    private String body;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "templateId='" + templateId + '\'' +
                ", language=" + language +
                ", type=" + type +
                ", email='" + email + '\'' +
                '}';
    }
}
