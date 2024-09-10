package com.LoginWebApp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    protected static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public static String hashPassword(String password) {
        return encoder.encode(password);
    }
    
    public static boolean checkPassword(String rawPassword, String encodedPassword) {
    	System.out.println("Raw password: " + rawPassword + ", encoded password: " + encodedPassword);
        return encoder.matches(rawPassword, encodedPassword);
    }
}