package com.wakati.config;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public String getCurrentLanguage() {
        // later from JWT or SecurityContext
        return "en"; // fallback
    }
}