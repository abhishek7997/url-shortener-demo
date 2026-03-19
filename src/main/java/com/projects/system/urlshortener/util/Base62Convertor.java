package com.projects.system.urlshortener.util;

public class Base62Convertor {
    private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String convertToBase62(long num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            int r = (int) (num%62);
            sb.append(CHARSET.charAt(r));
            num /= 62;
        }

        return sb.reverse().toString();
    }
}
