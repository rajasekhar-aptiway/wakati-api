package com.wakati.util;

import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils {

    public static String getBearerToken(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }
}