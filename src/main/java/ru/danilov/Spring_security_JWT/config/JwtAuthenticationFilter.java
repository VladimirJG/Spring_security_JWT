package ru.danilov.Spring_security_JWT.config;


import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.danilov.Spring_security_JWT.security.JwtUtil;
import ru.danilov.Spring_security_JWT.service.PersonDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = extractTokenFromRequest(request);

        if (token != null && validateToken(token)) {
            Authentication authentication = createAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Логика извлечения токена из запроса (например, из заголовка Authorization)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return "Missing token in Authorization header";
    }

    private boolean validateToken(String token) {
        // Логика верификации токена
        try {
            String username = jwtUtil.validateToken(token);
            personDetailsService.loadUserByUsername(username);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private Authentication createAuthentication(String token) {
        // Логика создания объекта Authentication на основе токена
        UserDetails userDetails = extractUserDetailsFromToken(token);
        return new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }


    private UserDetails extractUserDetailsFromToken(String token) {
        // Логика извлечения информации о пользователе из токена
        String username = jwtUtil.validateToken(token);
        return personDetailsService.loadUserByUsername(username);
    }
}