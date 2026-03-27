package com.wakati.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RefreshTokenUtil {

    private static final SecureRandom random = new SecureRandom();

    // ✅ Generate plain token
    public static String generatePlain() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return bytesToHex(bytes);
    }

    // ✅ Hash token (SHA-256)
    public static String hash(String plain) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(plain);
    }

    // ✅ Expiry
    public static LocalDateTime expiresInDays(int days) {
        return LocalDateTime.now().plus(days, ChronoUnit.DAYS);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}