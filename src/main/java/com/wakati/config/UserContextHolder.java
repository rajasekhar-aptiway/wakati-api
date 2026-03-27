package com.wakati.config;

import com.wakati.entity.User;

public class UserContextHolder {

    private static final ThreadLocal<User> context = new ThreadLocal<>();

    public static void setUser(User user) {
        context.set(user);
    }

    public static User getUser() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}