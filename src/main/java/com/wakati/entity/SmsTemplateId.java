package com.wakati.entity;

import com.wakati.enums.Language;

import java.io.Serializable;
import java.util.Objects;

public class SmsTemplateId implements Serializable {

    private String templateKey;
    private Language language;

    public SmsTemplateId() {}

    public SmsTemplateId(String templateKey, Language language) {
        this.templateKey = templateKey;
        this.language = language;
    }

    // equals & hashCode (MANDATORY)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmsTemplateId)) return false;
        SmsTemplateId that = (SmsTemplateId) o;
        return Objects.equals(templateKey, that.templateKey) &&
               language == that.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateKey, language);
    }
}