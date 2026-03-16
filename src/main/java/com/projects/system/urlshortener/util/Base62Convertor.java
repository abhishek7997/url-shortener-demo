package com.projects.system.urlshortener.util;

import java.math.BigInteger;

public class Base62Convertor {
    private static final String SPACE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String convertToBase62(long num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            long r = num%62;
            sb.append(SPACE.charAt((int) r));
            num /= 62;
        }

        return sb.toString();
    }

    public static String convertToBase62(BigInteger num) {
        StringBuilder sb = new StringBuilder();

        while (num.compareTo(BigInteger.ZERO) > 0) {
            int r = num.mod(BigInteger.valueOf(62)).intValue();
            sb.append(SPACE.charAt(r));
            num = num.divide(BigInteger.valueOf(62));
        }

        return sb.toString();
    }
}
