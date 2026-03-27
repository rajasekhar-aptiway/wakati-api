package com.wakati.model.response.DTO;


public class AttributeDTO {
    private String attributeId;
    private String attributeKey;
    private String attributeValue;

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public AttributeDTO(String attributeId, String attributeKey, String attributeValue) {
        this.attributeId = attributeId;
        this.attributeKey = attributeKey;
        this.attributeValue = attributeValue;
    }
}