package com.wakati.entity;

import jakarta.persistence.*;
import com.wakati.enums.Language;

import java.util.Date;

@Entity
@Table(name = "SMS_TEMPLATES")
@IdClass(SmsTemplateId.class)
public class SmsTemplate {

    @Id
    @Column(name = "template_key", length = 60)
    private String templateKey;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "updated_by", length = 36)
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    // ===== Lifecycle =====

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    // ===== Getters & Setters =====

    public String getTemplateKey() { return templateKey; }
    public void setTemplateKey(String templateKey) { this.templateKey = templateKey; }

    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public Date getUpdatedAt() { return updatedAt; }
    public Date getCreatedAt() { return createdAt; }

    // ===== toString =====

    @Override
    public String toString() {
        return "SmsTemplate{" +
                "templateKey='" + templateKey + '\'' +
                ", language=" + language +
                ", isActive=" + isActive +
                '}';
    }
}