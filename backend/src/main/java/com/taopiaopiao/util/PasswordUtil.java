package com.taopiaopiao.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具类 - 使用BCrypt
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * BCrypt加密密码
     */
    public static String encrypt(String password) {
        if (password == null) {
            return null;
        }
        return encoder.encode(password);
    }

    /**
     * 验证密码
     */
    public static boolean verify(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encodedPassword);
    }
}
