package com.wakati.config;

import io.jsonwebtoken.Claims;

public class JwtValidationResult {

    private boolean valid;
    private String message;
    private Claims payload;

    public static JwtValidationResult valid(Claims payload) {
        JwtValidationResult r = new JwtValidationResult();
        r.valid = true;
        r.payload = payload;
        return r;
    }

    public static JwtValidationResult invalid(String message) {
        JwtValidationResult r = new JwtValidationResult();
        r.valid = false;
        r.message = message;
        return r;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Claims getPayload() {
        return payload;
    }

    public void setPayload(Claims payload) {
        this.payload = payload;
    }
}