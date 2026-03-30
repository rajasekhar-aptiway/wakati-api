package com.wakati.util;

import java.security.SecureRandom;

public class PasswordUtil {

    public static String generatePassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String all = upper + lower + digits;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Ensure at least one of each
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));

        // Fill remaining
        for (int i = 3; i < length; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }

        // Shuffle
        return shuffle(password.toString());
    }

    private static String shuffle(String input) {
        char[] chars = input.toCharArray();
        SecureRandom random = new SecureRandom();

        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }
}
