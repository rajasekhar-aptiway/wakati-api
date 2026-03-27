package com.wakati.entity;

import java.io.Serializable;
import java.util.Objects;

public class EmailTemplateId implements Serializable {

    private String templateKey;
    private String language;

    public EmailTemplateId() {}

    public EmailTemplateId(String templateKey, String language) {
        this.templateKey = templateKey;
        this.language = language;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailTemplateId)) return false;
        EmailTemplateId that = (EmailTemplateId) o;
        return Objects.equals(templateKey, that.templateKey) &&
                language == that.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateKey, language);
    }
}
