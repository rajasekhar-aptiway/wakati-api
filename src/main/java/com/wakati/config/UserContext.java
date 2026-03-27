package com.wakati.config;

import com.wakati.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public String getCurrentLanguage() {
        User user = UserContextHolder.getUser();
        if(user != null && user.getPreferredLanguage() != null){
            return user.getPreferredLanguage();
        }
        return "en";
    }
}