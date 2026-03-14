package com.example.AiResumeMatcher.utils;

public final class TextNormalizer {

    private TextNormalizer() {} // static only

    /**
     * Quick extra cleanup that can be called from anywhere (not Spring bean)
     */
    public static String removeExtraWhitespace(String text) {
        if (text == null) return "";
        return text.replaceAll("\\s+", " ").trim();
    }

    /**
     * Very simple check if string looks like a version or number pattern
     */
    public static boolean looksLikeVersion(String token) {
        return token != null && token.matches("(?i)[a-z]*\\s*\\d+(\\.\\d+)*");
    }

    // Add 1–2 more small helpers if you need them later (e.g. email/phone removal)
}