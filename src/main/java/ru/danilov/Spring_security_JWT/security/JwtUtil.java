package ru.danilov.Spring_security_JWT.security;

import org.springframework.security.core.userdetails.UserDetails;

public class JwtUtil {

    public static String generateToken(UserDetails userDetails) {
        // Логика генерации JWT
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        // Логика проверки JWT
    }
}
